# 1. Koordinatensysteme und Transformationen

In dieser Übung werden Sie Python-Funktionen für Koordinatentransformationen schreiben und diese dann anwenden, um Beispiele aus der Vorlesung nachzurechnen.
Sie werden auch lernen, Tests zu implementieren, um die Korrektheit Ihrer Implementierungen zu überprüfen.

Im Ordner ``exercise1`` finden Sie die Dateien ``transformations.py``, ``exercise1_2.py``, ``exercise1_3.py`` und einen Ordner ``exercise1_tests``.
Um Ihnen den Einstieg zu erleichtern, gibt es in den Dateien ``transformations.py``, ``exercise1_2.py`` und ``exercise1_3.py`` vorgefertigte Funktionsrümpfe, in denen Sie Ihre Lösung implementieren sollen.
Auch für die Tests sind in ``exercise1_tests/test_exercise1_1.py``, ``exercise1_tests/test_exercise1_2.py`` und ``exercise1_tests/test_exercise1_3.py`` Funktionsrümpfe implementiert, in die Sie Ihre Tests einfügen sollen.
Es sind auch schon einige Beispieltests implementiert, an denen Sie sich orientieren können.

## 1.1 Pythonfunktionen für Koordinatentransformationen

Zur Berechnung von Rotations- und Transformationsmatrizen müssen die folgenden Funktionen in Python implementiert werden.
Benutzen Sie dazu den Datentyp `array` aus dem Paket `numpy`.

| Funktion       | Beschreibung                                                                                                                        |
|----------------|-------------------------------------------------------------------------------------------------------------------------------------|
| `rot(theta)`   | liefert eine 2D-Rotationsmatrix mit Drehwinkel theta zurück.                                                                        |
| `rotx(theta)`  | liefert eine elementare 3D-Rotationsmatrix mit Drehwinkel theta um Drehachse `x` zurück.                                            |
| `roty(theta)`  | liefert eine elementare 3D-Rotationsmatrix mit Drehwinkel theta um Drehachse `y` zurück.                                            |
| `rotz(theta)`  | liefert eine elementare 3D-Rotationsmatrix mit Drehwinkel theta um Drehachse `z` zurück.                                            |
| `rot2trans(r)` | wandelt die Rotationsmatrix `r` in eine homogene Transformationsmatrix um und liefert diese zurück.                                 |
| `trans(t)`     | liefert eine homogene Translationsmatrix mit Translation `t` zurück. `t` ist ein Tupel der Größe 2 bzw. 3 für den 2D- bzw. 3D-Fall. |

In `transformations.py` befinden sich die vorbereiteten Funktionsrümpfe, die Sie nach der vorigen Funktionsbeschreibung vervollständigen sollen.

Im root des Repositories können sie mit dem folgenden Befehl testen, ob Ihre Implementierung korrekt ist.

```bash
python3 -m pytest --no-header -v --tb=short test_exercise1_1.py
```

## 1.2 Anwendung der Koordinatentransformationen im 3D

Verwenden Sie die Funktionen in ``transformations.py``, um die Aufgabe 2.1 aus der Vorlesung zu lösen.
Implementieren Sie zuerst die erforderlichen Tests in ``exercise1_tests/test_exercise1_2.py``, um die Lösungen der Aufgabe 2.1 bereits während der Implementierung testen zu können.
Sie können die Korrektheit Ihrer Tests überprüfen, indem Sie diese in den ``exercise1``-Branch committen und pushen.
Die CI-Pipeline testet dann Ihre Tests und überprüft, ob sie korrekt implementiert sind.

```bash
python3 -m pytest --no-header -v --tb=short test_exercise1_2.py
```

## 1.3 Anwendung der Koordinatentransformationen im 2D

Verwenden Sie die Funktionen in ``transformations.py``, um die Aufgabe 2.2 aus der Vorlesung zu lösen.
Implementieren Sie zuerst die erforderlichen Tests in ``exercise1_tests/test_exercise1_3.py``, um die Lösungen der Aufgabe 2.2 bereits während der Implementierung testen zu können.
Sie können die Korrektheit Ihrer Tests überprüfen, indem Sie diese in den ``exercise1``-Branch committen und pushen.
Die CI-Pipeline testet dann Ihre Tests und überprüft, ob sie korrekt implementiert sind.

Implementieren Sie die Lösung zur Aufgabe 2.2 aus der Vorlesung in ``exercise1_3.py``.
Im Root des Repositories können Sie mit folgendem Befehl testen, ob Ihre Implementierung korrekt ist:

```bash
python -m pytest --no-header -v --tb=short test_exercise1_3.py
```

## Abgabe

Bearbeiten Sie die Aufgabe mit ihrem Team in Branch `exercise1`.
Sobald Sie fertig sind, stellen Sie einen Merge Request gegen den Branch `main`.
Die Abgabe gilt als bestanden, wenn die CI-Tests ohne Fehler durchlaufen und der Merge-Request akzeptiert wurde.
