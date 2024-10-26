-- INSERTS
INSERT INTO Land (landname)
VALUES ('Deutschland');

INSERT INTO Land (landname)
VALUES ('Frankreich');

INSERT INTO Land (landname)
VALUES ('Spanien');

INSERT INTO Land (landname)
VALUES ('Italien');

INSERT INTO Land (landname)
VALUES ('Indien');

INSERT INTO Land (landname)
VALUES ('Ägypten');

INSERT INTO Land (landname)
VALUES ('Marokko');

--Bsp für Deutschland
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (5378, 'Deutschland', 4, 'Friedrichstrasse', 10117, 'Berlin');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('emma@beispiel.com', 5378, 'Emma', 'Eckert', '1', 'pw123!', 'DE12345678901234567890');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('sarah@beispiel.com', 5378, 'Sarah', 'Singer', '1', 'sa1!rah', 'DE12345678900987654321');

-- Bsp für Frankreich
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (4316, 'Frankreich', 10, 'Rue de la Liberté', 75001, 'Paris');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('sophie@example.com', 4316, 'Sophie', 'Dubois', '1', 'pass123', 'FR12345678901234567890');
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (67246, 'Frankreich', 61, 'Rue d-egalité', 75003, 'Marseille');

-- Bsp für Spanien
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (6539, 'Spanien', 25, 'Calle de la Playa', 28001, 'Barcelona');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('carlos@example.com', 6539, 'Carlos', 'García', '0', 'secret', 'ES98765432109876543210');
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (093577, 'Spanien', 68, 'Playa', 28002, 'Madrid');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('bernd@example.com', 093577, 'Bernd', 'Brotler', '1', 'vsh!hU?m', 'DE8205432103176548910');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('lara@example.com', 093577, 'Lara', 'Lang', '1', 'lar4r', 'DE8205408313176549630');
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (2791, 'Spanien', 88, 'strass de la Playa', 28003, 'Barcelona');
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (76, 'Spanien', 58, 'Playa', 28004, 'Barcelona');

-- Bsp für Italien
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (0938, 'Italien', 8, 'Via Roma', 00184, 'Rom');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('giulia@example.com', 0938, 'Giulia', 'Rossi', '1', 'gno45cchi', 'IT8765432109876543210');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('giovanni@example.com', 0938, 'Giovanni', 'Calabrese', '0', 'spag56?eti', 'IT87654321063680272561');

-- Bsp für Indien
INSERT INTO Adresse (adressID, landname, nr, strasse, plz, stadtname)
VALUES (9063, 'Indien', 15, 'MG Road', 560001, 'Bangalore');
INSERT INTO Kunde (mail, adressID, vorname, nachname, newsletter, passwort, iban)
VALUES ('vikram@example.com', 9063, 'Vikram', 'Patel', '1', 'ta2j!ma', 'IN01234567890123456789');

-- Ferienwohnung in Deutschland
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (6226, 5378, 'Gemütliche Stadtwohnung', 2, 70.50, 1200.00);

-- Ferienwohnung in Frankreich
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (82812, 4316, 'Charmantes Apartement am Fluss', 3, 75.0, 2600);
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (6742900, 67246, 'Le rat', 2, 30.0, 800);

-- Ferienwohnung in Spanien
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (1298, 6539, 'Strandvilla', 5, 65.5, 3500.00);
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (7126, 093577, 'Spanische Finka', 4, 55.0, 4200.00);
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (644890, 2791, 'Spanische Villa', 6, 155.0, 8200.00);
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (8, 76, 'Ein Haus in Spanien', 4, 100.0, 2300.00);

-- Ferienwohnung in Italien
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (712689, 0938, 'Gemütliche Toskana-Villa', 3, 80.0, 1000.00);

-- Ferienwohnung in Indien
INSERT INTO Ferienwohnung (ferienwohnungID, adressID, ferienwohnungname, zimmer, groesse, preis)
VALUES (1762, 9063, 'Exotisches Strandhaus', 2, 70.5, 400.00);

-- Buchung und Anzahlung für Deutschland
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (3617, 'emma@beispiel.com', 6226, 0, '10.05.2024', '30.05.2024', '13.01.2024', 5, '02.06.2024', '10.05.2024', 2500);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (85768, 3617, '02.06.2024', 1200);

DELETE FROM Anzahlung WHERE buchungsnr = '3617';
DELETE FROM Buchung WHERE buchungsnr = '3617';

-- Buchung und Anzahlung für Frankreich
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (1675, 'sophie@example.com', 82812, 0, '01-05-2024', '07-05-2024', '20-04-2024', 4, '08-05-2024', '10-05-2024', 500);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (6292, 1675, '21-04-2024', 2600);

-- Buchung und Anzahlung für Spanien
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, stornodatum, beginn, ende, buchungsdatum, rechungsdatum, betrag)
VALUES (2233, 'carlos@example.com', 1298, 1, '03.06.2024', '15.06.2024', '20.06.2024', '30-05-2024', '23-06-2024', 700);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (5732, 2233, '23.06.2024', 3500);
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (369, 'bernd@example.com', 7126, 0, '10-10-2024', '25-10-2024', '10-04-2024', 5, '30-10-2024', '29-10-2024', 600);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (753269, 369, '29-10-2024', 1000);
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (644890, 'bernd@example.com', 7126, 0, '10-09-2024', '25-09-2024', '01-04-2024', 3, '14-10-2024', '15-10-2024', 1600);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (52005, 644890, '15-10-2024', 1560);
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (368109, 'lara@example.com', 7126, 0, '12-10-2024', '23-10-2024', '10-01-2024', 5, '30-10-2024', '01-10-2024', 3000);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (79009, 368109, '01-10-2024', 1500);

-- Buchung und Anzahlung für Italien
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (36809, 'giulia@example.com', 712689, 0, '10-07-2024', '20-07-2024', '25-06-2024', 3, '21-07-2024', '25-07-2024', 600);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (87634, 36809, '26-06-2024', 1000);

-- Buchung und Anzahlung für Indien
INSERT INTO Buchung (buchungsnr, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechungsdatum, betrag)
VALUES (46548, 'vikram@example.com', 1762, 0, '05-08-2024', '15-08-2024', '20-07-2024', 1, '16-08-2024', '18-08-2024', 800);
INSERT INTO Anzahlung (anzahlungsID, buchungsnr, zahlungsdatum, betrag)
VALUES (8767, 46548, '21-07-2024', 400);

-- Touriatt Deutschland
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('Brandenburger Tor', 'Ein historisches Wahrzeichen in Berlin.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (6226, 'Brandenburger Tor', 1.5);

-- Touriatt Frankreich
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('Eiffelturm', 'Ein ikonischer Turm in Paris.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (82812, 'Eiffelturm', 0.8);
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('Vieux-Port', 'Historisches und kulturelles Zentrum Marseilles.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (6742900, 'Vieux-Port', 0.3);

-- Touriatt Spanien
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('La Sagrada Família', 'Eine beeindruckende, unvollendete Basilika in Barcelona.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (1298, 'La Sagrada Família', 2.3);
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('Museo Nacional del Prado', 'Eine beeindruckendes Nationalmuseum.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (7126, 'Museo Nacional del Prado', 4.3);

-- Touriatt Italien
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('Kolosseum', 'Ein antikes Amphitheater in Rom.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (712689, 'Kolosseum', 1.2);

-- Touriattr Indien
INSERT INTO Touristenattraktion (touriattname, beschreibung)
VALUES ('Taj Mahal', 'Ein atemberaubendes Mausoleum in Agra.');
INSERT INTO In_der_Naehe (ferienwohnungID, touriattname, entfernung) 
VALUES (1762, 'Taj Mahal', 3.5);

INSERT INTO Austattung (austattungsname)
VALUES ('Küche');
INSERT INTO Austattung (austattungsname)
VALUES ('Balkon');
INSERT INTO Austattung (austattungsname)
VALUES ('Terasse');
INSERT INTO Austattung (austattungsname)
VALUES ('Fernseher');
INSERT INTO Austattung (austattungsname)
VALUES ('Parkplatz');
INSERT INTO Austattung (austattungsname)
VALUES ('Sauna');
INSERT INTO Austattung (austattungsname)
VALUES ('Pool');
INSERT INTO Austattung (austattungsname)
VALUES ('Innenhof');
INSERT INTO Austattung (austattungsname)
VALUES ('WLAN');

-- Austattung für Deutschland 
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Küche', 6226);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Balkon', 6226);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Fernseher', 6226);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('WLAN', 6226);

--Austattung für Frankreich
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Balkon', 82812);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Fernseher', 6742900);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Terasse', 6742900);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Küche', 6742900);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Pool', 82812);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Küche', 82812);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('WLAN', 82812);

--Austattung für Spanien
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Pool', 1298);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Küche', 1298);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('WLAN', 1298);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Sauna', 644890);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Balkon', 644890);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Fernseher', 644890);

--Austattung für Italien
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Terasse', 7126);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Fernseher', 7126);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Küche', 7126);

--Austattung für Indien
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('Innenhof', 1762);
INSERT INTO beinhaltet (austattungsname, ferienwohnungID) VALUES ('WLAN', 1762);

-- Bild für Deutschland
INSERT INTO Bild (link, ferienwohnungID) 
VALUES ('berlin_ferienwohnung1.jpg', 6226);

-- Bild für Frankreich
INSERT INTO Bild (link, ferienwohnungID)
VALUES ('paris_ferienwohnung1.jpg', 82812);
INSERT INTO Bild (link, ferienwohnungID)
VALUES ('marseille_ferienwohnung1.jpg', 6742900);

-- Bild für Spanien
INSERT INTO Bild (link, ferienwohnungID)
VALUES ('barcelona_ferienwohnung1.jpg', 1298);
INSERT INTO Bild (link, ferienwohnungID)
VALUES ('spanien_ferienwohnung2.jpg', 7126);

-- Bild für Italien
INSERT INTO Bild (link, ferienwohnungID)
VALUES ('rom_ferienwohnung1.jpg', 712689);

--Bild für Indien
INSERT INTO Bild (link, ferienwohnungID)
VALUES ('bangalore_ferienwohnung1.jpg', 1762);

COMMIT;

SELECT user FROM dual;

SELECT * FROM all_tables WHERE table_name = 'KUNDE';

SELECT * FROM Kunde WHERE mail = 'bernd@example.com';

SELECT * FROM KUNDE;

SELECT * FROM Ferienwohnung;

SELECT * FROM Buchung;

-- a) Wie viele Ferienwohnungen wurden noch nie gebucht?
SELECT COUNT (*) AS ungebuchte_ferienwohnungen
FROM Ferienwohnung f
WHERE f.ferienwohnungID NOT IN (SELECT ferienwohnungID FROM Buchung);

-- b) Welche Kunden haben EINE Ferienwohnung bereits mehrmals gebucht?
SELECT k.nachname, b.ferienwohnungID, COUNT(*) as buchung_count
FROM Kunde k JOIN Buchung b ON k.mail = b.mail
GROUP BY k.nachname, b.ferienwohnungID
HAVING COUNT(b.buchungsnr) > 1;

-- c) Welche Ferienwohnungen in Spanien haben durchschnittlich mehr als 4 Sterne erhalten?
SELECT f.ferienwohnungID, f.ferienwohnungname, ROUND(AVG(b.sterne), 3) AS durchschnittlich_4sterne
FROM Ferienwohnung f JOIN Adresse a ON f.adressID = a.adressID JOIN Buchung b ON f.ferienwohnungID = b.ferienwohnungID
WHERE a.landname = 'Spanien' AND b.sterne IS NOT NULL
GROUP BY f.ferienwohnungID, f.ferienwohnungname
HAVING AVG(b.sterne) > 4;

/* d) Welche Ferienwohnung hat die meisten Ausstattungen? Falls mehrere Ferienwohnungen das Maximum
    an Ausstattungen erreichen, sollen diese alle ausgegeben werden*/
SELECT f.ferienwohnungID, COUNT(*) AS anzahl_ausstattungen
FROM Ferienwohnung f
JOIN Austattung a ON f.ferienwohnungID = a.ferienwohnungID
GROUP BY f.ferienwohnungID
HAVING COUNT(*) = (
    SELECT MAX(anzahl)
    FROM (
        SELECT COUNT(*) AS anzahl
        FROM Ferienwohnung f2
        JOIN Austattung a2 ON f2.ferienwohnungID = a2.ferienwohnungID
        GROUP BY f2.ferienwohnungID
    )
);



/* e) Wie viele Reservierungen gibt es für die einzelnen Länder? Länder, in denen keine Reservierungen existieren
    sollen mit der Anzahl 0 ebenfalls aufgeführt werden. Das Ergebnis soll nach der Anzahl der 
    Reservierungen absteigend sortiert werden */
SELECT l.landname, COALESCE(COUNT(b.buchungsnr), 0) AS reservierungen
FROM Land l JOIN Adresse a ON a.landname = l.landname
JOIN Ferienwohnung f ON a.adressID = f.adressID
JOIN Buchung b ON f.ferienwohnungID = b.ferienwohnungID
GROUP BY l.landname
ORDER BY reservierungen DESC;

--f) Wie viele Ferienwohnungen sind pro Stadtname gespeichert?
SELECT a.stadtname, COUNT(f.ferienwohnungID) AS ferienwohnunen_pro_Stadtname
FROM Ferienwohnung f JOIN Adresse a ON a.adressID = f.adressID
GROUP BY a.stadtname;

--g) Welche Ferienwohnungen haben bisher nur Bewertungen mit einem Stern erhalten?
SELECT f.ferienwohnungID, f.ferienwohnungname
FROM Ferienwohnung f JOIN Buchung b ON f.ferienwohnungID = b.ferienwohnungID
WHERE b.sterne IS NOT NULL
GROUP BY f.ferienwohnungID, f.ferienwohnungname
HAVING MAX(b.sterne) = 1;

/*h) Welche Ferienwohnungen mit Sauna sind in Spanien in der Zeit vom 01.05.2024 - 21.05.2024 noch frei?
    Geben Sie den Ferienwohnungsname und deren durchschnittliche Bewertung an.
    Ferienwohnungen mit guten Bewertungen sollen zuerst angezeigt werden. 
    Ferienwohnungen ohne Bewertungen sollen am Ende ausgegeben werden.*/
WITH freie_wohnungen AS (
    SELECT fw.ferienwohnungID, fw.ferienwohnung_name, fw.adressID
    FROM Ferienwohnung fw
    JOIN Buchung b ON fw.ferienwohnungID = b.ferienwohnungID
    AND NOT (b.ende < TO_DATE('01-05-2024', 'DD-MM-YYYY') OR b.beginn > TO_DATE('21-05-2024', 'DD-MM-YYYY'))
    WHERE b.buchungsnr IS NULL
)

SELECT fw.ferienwohnung_name, AVG(b.sterne) AS durchschnittliche_bewertung
FROM freie_wohnungen fw
JOIN Adresse a ON fw.adressID = a.adressID
JOIN beinhaltet bi ON fw.ferienwohnungID = bi.ferienwohnungID
JOIN Buchung b ON fw.ferienwohnungID = b.ferienwohnungID
WHERE a.landname = 'Spanien'
AND bi.ausstattungsname = 'Sauna'
GROUP BY fw.ferienwohnungID, fw.ferienwohnung_name
ORDER BY durchschnittliche_bewertung DESC NULLS LAST;
