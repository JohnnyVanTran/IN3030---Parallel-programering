import pandas as pd
from matplotlib import pyplot as plt

data = pd.read_csv("data.csv", sep=";")
print(data)
plt.plot(data["N "], data["synchro"], label="synchro")
plt.plot(data["ind"], data["parallel"], label="parallel")
plt.legend()
plt.yscale("log")
plt.show()