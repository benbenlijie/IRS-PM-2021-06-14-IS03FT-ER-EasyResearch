import pandas as pd
import numpy as np
from pathlib import Path
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.metrics.pairwise import cosine_similarity


class PaperRecommandation(object):
    KEY_COLUMN = "keyword1"
    SIMILARITY_THRESHOLD = 0.1
    OUTPUT_COLUMNS = [("title", "title"), ("authors_parsed", "author"), ("update_date", "date"), ("id", "url")]

    def __init__(self, paper_data_folder):
        # print(paper_data_folder)
        self.paper_dfs = []
        folder = Path(paper_data_folder)
        for csv_file in folder.glob("*.csv"):
            # print("read from", csv_file)
            df = pd.read_csv(str(csv_file)).fillna("")
            df = df.rename(columns={a: b for a, b in self.OUTPUT_COLUMNS})
            # for col1, col2 in self.OUTPUT_COLUMNS:
            #     df[col2] = df[col1]
            select_columns = [a[1] for a in self.OUTPUT_COLUMNS] + [self.KEY_COLUMN, ]

            df = df[select_columns]
            df["url"] = df["url"].apply(lambda x: f"https://arxiv.org/abs/{x}")
            # print(df["url"].head())
            cv = CountVectorizer()
            count_vectors = cv.fit_transform(df[self.KEY_COLUMN])
            test_vect = cv.transform(["speech"])
            self.paper_dfs.append([df, cv, count_vectors, test_vect])

    def contentBasedRecommand(self, keyword, max_count=10):
        results = []
        for df, cv, vectors, test_vect in self.paper_dfs:

            keyword_vector = cv.transform([keyword])

            cosine_sim = cosine_similarity(vectors, keyword_vector)
            index = np.argsort(cosine_sim.reshape(-1))[::-1]

            idx, _ = np.where(cosine_sim > self.SIMILARITY_THRESHOLD)

            count = min(max_count, len(idx))
            result_df = df.loc[index[:count], [a[1] for a in self.OUTPUT_COLUMNS]]
            result_df = result_df.applymap(lambda x: x if not isinstance(x, str) else x.replace("\n", ""))

            results.append(result_df)

        if len(results) > 1:
            return pd.concat(results)
        else:
            return results[0]




