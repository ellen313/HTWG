# MORO-Labor WS 2026

In diesen Übungen implementieren Sie grundlegende Funktionen, die für die Steuerung von Robotern erforderlich sind. Mit diesen Funktionen steuern Sie dann einen mobilen Roboter mit Greifarm. Dabei lernen Sie auch das Robot Operating System 2 (ROS2) kennen.
Programmiert wird in Python.

## Arbeitsweise

- Klonen Sie dieses Repo um die darin enthaltenen Aufgaben zu bearbeiten.
- Erstellen Sie für jede neue Aufgabe einen Branch nach dem Schema `exercise<Nummer>` (z.B. für Aufgabe 1: `exercise1`).
- Sobald Sie die Aufgabe bearbeitet haben stellen Sie einen Merge-Request gegen den Branch `main` in diesem Repo.
- Die Aufgabe gilt als bestanden, wenn die CI-Pipeline alle Tests erfolgreich ausgeführt hat und der Merge-Request akzeptiert wurde.

Sie können selbstverständlich weitere Sub-Branches zum Bearbeiten der Aufgaben erstellen. Lediglich der Merge-Request
mit Ihrer Abgabe muss von einem Branch mit Namen nach dem Schema `exercise<Nummer>` ausgehen.

## Implementierungshinweis

- Verwenden Sie für Funktionsaufrufe ausschließlich Parameter in SI-Einheiten.
Beispielsweise werden Entfernungen in Meter, Zeiten in Sekunden und Winkel in Radianten angegeben.
Sollten Sie Einheiten zwischenzeitlich nicht in SI-Einheiten verarbeiten, machen Sie dies
durch den Variablennamen kenntlich. Speichern Sie Winkel beispielsweise in Grad, können Sie
die zugehörige Variable beispielsweise `alpha_deg` nennen.

- Runden Sie Ergebnisse nur in den Tests nicht in den Funktionen.

## Arbeitsumgebung

Für die Übungen stehen Ihnen virtuelle Maschinen mit vorinstalliertem Ubuntu 24.04 und ROS2 Jazzy zur Verfügung.
Auf diesen Rechnern ist alles installiert, was Sie für die Übungen benötigen.

Um sich an der virtuellen Maschine anzumelden, verwenden Sie NoMachine.
Der NoMachine-Client ist auf allen Rechnern im Labor installiert, sodass Sie sich von jedem Rechner aus in Ihre virtuelle Maschine einloggen können.
Sie können NoMachine (https://www.nomachine.com/) auch auf Ihrem privaten Rechner installieren, um sich in die virtuelle Maschine einzuloggen.

Für die Programmierung bietet es sich an, VSCode mit der Remote-SSH Extension zu verwenden.
Dies erlaubtes beim Programmieren direkt auf der VM zu arbeiten und die volle Auflösung des lokalen Rechners zu verwenden.

<details>
<summary><b>Verwendung der Remote-SSH Extension:</b>
Klicken Sie <b>hier</b> um weitere Informationen zu erhalten.</summary>

Um VSCode lokal auf dem Laborrechner oder ihrem eignen Rechner zu nutzen und sich über die Remote-SSH Extension auf Ihrem virtuellen Rechner zu verbinden führen Sie die folgenden Schritte aus.

Erstellen Sie einen SSH-Key.
Führen Sie dazu in einer PowerShell folgenden Befehl aus:

```bash
ssh-keygen -t ed25519 -f "$HOME\.ssh\id_ed25519"
```

Autorisieren Sie Ihren Windows-Rechner für die Verbindung mit der VM, indem Sie Ihren lokalen öffentlichen Schlüssel auf den SSH-Host kopieren.
Führen Sie hierfür die folgenden Befehle in einer PowerShell auf ihrem Windows-Rechner aus und ersetzen Sie dabei den Benutzer- und Hostnamen mit denen Ihrer Gruppe (z.B. moro-1-ws26@141.37.29.161 für Team 1).

```bash
$USER_AT_HOST="moro-<team_id>-ws26@hostname"
$PUBKEYPATH="$HOME\.ssh\id_ed25519.pub"

$pubKey=(Get-Content "$PUBKEYPATH" | Out-String); ssh "$USER_AT_HOST" "mkdir -p ~/.ssh && chmod 700 ~/.ssh && echo '${pubKey}' >> ~/.ssh/authorized_keys && chmod 600 ~/.ssh/authorized_keys"
```

Für das Verbinden zu der VM in VSCode folgen sie der Dokumentation:
https://code.visualstudio.com/docs/remote/ssh#_connect-to-a-remote-host

</details>

## Abgaben

Die Merge-Request für die einzelnen Abgaben müssen bis zu folgenden Terminen gestellt sein.

- **Exercise 1** (28.10.2025)
- **Exercise 2** (11.11.2025)
- **Exercise 3** (25.11.2025)
- **Exercise 4** (15.12.2025)
- **Exercise 5** (20.01.2026)
