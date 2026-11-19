import pandas as pd
import matplotlib.pyplot as plt


#resolution

openmp_data = pd.read_csv("openmp_resolution.csv", sep=';')

widths =  openmp_data['width']
times =  openmp_data['time_ms']

#plot
plt.figure(figsize=(8, 6))
plt.plot(widths, times, marker='o', linestyle='-')
plt.title("OpenMP Mandelbrot-Berechnungen: Laufzeit - Auflösung")
plt.xlabel("WIDTH = HEIGHT")
plt.ylabel("Laufzeit (ms)")
plt.grid(True)

plt.savefig("plots/openmp_mandelbrot_resolution.png")
plt.show()

#zoom
zoom_data = pd.read_csv("openmp_zoom.csv", sep=';')

plt.figure(figsize=(8,6))
plt.bar(zoom_data['zoom_level'], zoom_data['time_ms'], color='skyblue')
plt.title("OpenMP Mandelbrot-Berechnungen: Laufzeit - Zoom-Level")
plt.xlabel("Zoom-Level")
plt.ylabel("Laufzeit (ms)")
plt.grid(axis='y')

for idx, val in enumerate(zoom_data['time_ms']):
    plt.text(idx, val + 50, f"{val:.0f}", ha='center', va='bottom')

plt.savefig("plots/openmp_mandelbrot_zoom.png")
plt.show()