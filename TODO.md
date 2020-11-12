# Views

- Add Prüfer
- Add Vorschrift / Sachnummer
- Modify Prüfung
- Modify Vorschrift / Sachnummer

#Functionality

- Update statements
    - Modify Prüfung
    - Modify Prüfer
        - Unterschrift from file!
    - Modify Vorschrift / Sachnummer
- Handle errors
    - On insertion: Sachnummer not existing
- Save to CSV (not required)
    - original format
    - own proper format
        - Separate files per table
- Load from CSV
    - own format
    - format of excel-file
- Persistent DB
- Error-logging to file
- Copyright notices
    - im Quelltext
- beim test: gerät nicht vorhanden -> fehler + frage ob es angelegt werden soll
    - wenn ja -> logdatei (pdf) beim anlegen erstellen (stetig erweiterte datei)


#Bugfixes

- Codes are scanned with `/` instead of `-` -> replace character in all kennzeichen strings!  