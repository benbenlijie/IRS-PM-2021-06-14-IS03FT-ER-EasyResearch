from django import forms
from django.contrib.auth.forms import UserCreationForm, AuthenticationForm
# from django.contrib.auth.models import User
from django.contrib.auth import get_user_model

User = get_user_model()
from django.db import models
from django.forms import ModelForm

CATEGORY_CHOICES = [
    ("CS.CV", "CV"),
    ("CS.CL", "NLP"),
    ("CS.SD", "AUDIO"),
    ("CS.ML", "MACHINE LEARNING"),
    ("CS.PL", "PROGRAMMING LANGUAGE"),
    ("CS.CR", "CRYPTOGRAPHY AND SECURITY"),
    ("CS.DS", "DATA STRUCTURES AND ALGORITHMS"),
    ("CS.DC", "DISTRIBUTED, PARALLEL, AND CLUSTER COMPUTING"),
    ("CS.HC","HUMAN-COMPUTER INTERACTION"),
]
SOURCE_CHOICES = [
    ("CVPR", "CVPR"),
    ("ICCV", "ICCV"),
    ("ECCV", "ECCV"),
    ("NIPS", "NIPS"),
    ("IEEE", "IEEE"),
    ("ICML", "ICML"),
    ("ICASSP","ICASSP"),
    ("INTERSPEECH","INTERSPEECH"),
    ("ACL","ACL"),
    ("EMNLP","EMNLP"),
    ("NAACL","NAACL"),
    ("AAAI","AAAI"),
    ("COLING","COLING"),
    ("ICLR","ICLR"),
]


class DateInput(forms.DateInput):
    input_type = 'date'


class NewUserForm(UserCreationForm):
    # optional fields
    email = forms.EmailField(required=False, max_length=254)

    def __init__(self, *args, **kwargs):
        super(NewUserForm, self).__init__(*args, **kwargs)

        for visible in self.visible_fields():
            visible.field.widget.attrs['class'] = 'form-input'

    class Meta:
        model = User
        fields = ('username', 'password1', 'password2', 'email',)


class PaperRecommandRequst(models.Model):
    author = models.CharField(max_length=100)
    source_preference = models.CharField(max_length=100, choices=SOURCE_CHOICES)
    category_preference = models.CharField(max_length=100, choices=CATEGORY_CHOICES)
    date_preference = models.DateField(blank=True, null=True, help_text="before published")
    keywords = models.CharField(max_length=100)

    def __str__(self):
        return self.author


class PaperRecommandRequstForm(ModelForm):
    # author = forms.CharField(max_length=100, required=False)
    # source_preference = forms.CharField(max_length=100, required=False)
    # keywords = forms.CharField(max_length=100, required=True)
    def __init__(self, *args, **kwargs):
        super().__init__(*args, **kwargs)
        for visible in self.visible_fields():

            if visible.name != "keywords":
                visible.field.required = False

            if isinstance(visible.field, forms.fields.DateField):
                visible.field.widget.attrs["class"] = "form-input-date"
            else:
                visible.field.widget.attrs['class'] = 'form-input'

    class Meta:
        model = PaperRecommandRequst
        fields = ["author", "source_preference", "category_preference", "date_preference", "keywords"]
        widgets = {
            'date_preference': DateInput(),
        }
