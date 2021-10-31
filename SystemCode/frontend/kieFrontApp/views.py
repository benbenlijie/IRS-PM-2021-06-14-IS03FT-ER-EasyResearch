from django.http import HttpResponse, HttpResponseRedirect
from django.views.generic import TemplateView
from requests.exceptions import HTTPError
from django.template import loader

from django.contrib.auth import login, authenticate
from django.contrib.auth.forms import UserCreationForm
from django.shortcuts import render, redirect
from django.contrib.auth import get_user_model

import requests
from json import dumps, loads
import datetime
from dateutil.relativedelta import relativedelta
import subprocess
import os
import stat
from .paper_recommand_py.recommandation import PaperRecommandation

import csv
import json
import threading
import pandas as pd
from pathlib import Path

User = get_user_model()
# TODO: create new forms
from .forms import NewUserForm, PaperRecommandRequstForm, CATEGORY_CHOICES, SOURCE_CHOICES

recommander = PaperRecommandation(str((Path(".") / "kieFrontApp/paper_recommand_py").resolve()))


def index(request):
    return HttpResponse("Nothing to see here. You're at the app index.")


def signup(request):
    if (request.user.is_authenticated):
        return redirect('/home/')

    if request.method == 'POST':
        form = NewUserForm(request.POST)
        if form.is_valid():
            form.save()
            username = form.cleaned_data.get('username')
            raw_password = form.cleaned_data.get('password1')
            user = authenticate(username=username, password=raw_password)
            login(request, user)
            return redirect('/home/')
    else:
        form = NewUserForm()
    return render(request, 'signup.html', {'form': form})


def executeJar(pathJar, configFileName, pathOutfile):
    print(pathJar)
    outfile = open(pathOutfile, 'w')
    p = subprocess.Popen(['java', '-jar', pathJar, configFileName], stdout=outfile, stderr=outfile, shell=False)
    # shell must be False for unix (See https://stackoverflow.com/questions/2400878/why-subprocess-popen-doesnt-work-when-args-is-sequence)
    outfile.close()


def pythonRecommand(keywords, path_out_file):
    print("start python recommand, ", keywords)
    result_df = recommander.contentBasedRecommand(keywords)
    result_df.to_csv(path_out_file, index=False)


def getOptaDir():
    current_dir = Path(".").resolve()
    dir_path = current_dir / "optaPlanner"
    dir_path = dir_path.resolve()
    return dir_path


class HomePageView(TemplateView):
    def get(self, request, **kwargs):
        if (request.user.is_authenticated):
            return render(request, 'home.html', context=None)
        else:
            return redirect('/login/')


# TODO: implement
class ComingSoonPageView(TemplateView):
    def get(self, request, **kwargs):
        return render(request, 'comingSoon.html', context=None)


class DebugPageView(TemplateView):
    def get(self, request, **kwargs):
        # import pdb; pdb.set_trace()
        return render(request, 'debug.html', context=None)


class TestQueryView(TemplateView):
    def get(self, request, **kwargs):
        query_type = request.GET.get("queryType")
        base_url = request.GET.get("base")
        auth = request.GET.get("auth")
        auth_pw = request.GET.get("auth_pw")
        payload = request.GET.get("payload")

        accept_header = "application/json"
        content_header = "application/json"
        headers = {
            'Accept': accept_header,
            'Content-Type': content_header
        }

        try:
            if (query_type == 1):
                response = requests.get(url=base_url, auth=(auth, auth_pw), headers=headers)
            else:
                response = requests.post(url=base_url, auth=(auth, auth_pw), headers=headers, json=payload)

            context = {}
            json_response = response.json()
            print(json_response)

            template = loader.get_template('wait.html')
            return HttpResponse(template.render(context, request))

        except HTTPError as http_err:
            print(f'HTTP error occurred: {http_err}')
        except Exception as err:
            print(f'Other error occurred: {err}')


class ShowRecommandResultView(TemplateView):
    PAPER_INFO = ["title", "author", "date", "url"]

    def parse(self, file):
        print(file)
        if file.is_file():
            try:
                df = pd.read_csv(file)
                df = df[df.columns.intersection(self.PAPER_INFO)]
                print("df length: ", len(df))
                if len(df) > 0:
                    return df.to_dict("records"), True
            except Exception as e:
                pass
            return [], False
        else:
            return [], True

    def get(self, request, **kwargs):
        if request.user.is_authenticated:
            dir_path = getOptaDir()
            file_id = "".join(e for e in request.user.username if e.isalnum())

            result_file = dir_path / f"{file_id}_results.txt"

            results, finished = self.parse(result_file)
            print("result and finished", result_file, len(results), finished)

            if finished:
                return render(request, "viewRecommand.html",
                              context={
                                  "results": results
                              })
            else:
                return render(request, "waitingPage.html",
                              context={
                                  "done": min(len(results), 7),
                                  "text": "The system is searching papers for you"
                              })
        else:
            return redirect('/login/')


# TODO: implement
class CreateRecommandationView(TemplateView):

    def get(self, request, *args, **kwargs):
        if request.user.is_authenticated:
            u = request.user
            # TODO: handle the user data
            form = PaperRecommandRequstForm()

            return render(request, "createRecommandation.html",
                          context={
                              "form": form
                          })
        else:
            return redirect('/login/')

    def post(self, request, **kwargs):
        feature_fields = ["author", "source_preference", "category_preference", "date_preference", "keywords"]
        if request.user.is_authenticated:
            form = PaperRecommandRequstForm(request.POST)
            if not form.is_valid():
                return redirect('/createRecommand/')

            dir_path = getOptaDir()
            file_id = "".join(e for e in request.user.username if e.isalnum())

            input_file = dir_path / f"{file_id}_input.txt"
            result_file = dir_path / f"{file_id}_results.txt"
            if result_file.is_file():
                open(str(result_file), 'w').close()
            else:
                result_file.touch(exist_ok=True)

            base_jar = False
            for field in feature_fields:
                value = form.cleaned_data.get(field)
                base_jar = base_jar or ((value != "" and value is not None) and field != "keywords")

            if base_jar:
                jar_path = dir_path / "optaplanner.jar"

                # TODO: create input file
                with open(input_file, 'w') as f:
                    for field in feature_fields:
                        f.write(f"{field}\n{form.cleaned_data.get(field)}\n")

                st = os.stat(jar_path)
                os.chmod(jar_path, st.st_mode | stat.S_IXUSR | stat.S_IXGRP | stat.S_IXOTH)

                threading.Thread(target=executeJar, args=(str(jar_path), str(input_file), str(result_file))).start()
            else:
                threading.Thread(target=pythonRecommand,
                                 args=(form.cleaned_data.get("keywords"), str(result_file))).start()
            return HttpResponseRedirect("/viewRecommand/")

        else:
            return redirect('/login/')
