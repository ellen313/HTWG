INSERT INTO Land(landname)
	VALUES('Deutschland');
INSERT INTO Land(landname)
	VALUES('Spanien');
INSERT INTO Land(landname)
	VALUES('Griechenland');
INSERT INTO Land(landname)
	VALUES('Österreich');
INSERT INTO Land(landname)
	VALUES('Frankreich');
INSERT INTO Land(landname)
	VALUES('Italien');
INSERT INTO Land(landname)
	VALUES('USA');

--Personen mit ihren Adressen (Kontodaten)

/* Max Mustermann */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
    VALUES ( 1234,'Deutschland', 'Musterstrasse', 1, 12345, 'Musterstadt');
INSERT INTO Kunde(mail, adressID, vorname, nachname, newsletter, passwort, iban)
    VALUES ('maxmustermann@example.com',1234, 'Max', 'Mustermann','Y','passwort123','DE12345678901234567');

/*Erika Musterfrau */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (34567, 'Deutschland', 'Musterweg', 2, 54321, 'Musterstadt');
INSERT INTO Kunde(mail, adressID, vorname, nachname, newsletter, passwort, iban)
	VALUES ('erikamusterfrau@example.com',34567, 'Erika', 'Musterfrau', 'Y', 'passwort456', 'DE09876543210987654321');

/*John Doe */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (35789, 'USA', 'Main Street', 123, 90210, 'Los Angeles');
INSERT INTO Kunde(mail, adressID, vorname, nachname, newsletter, passwort, iban)
	VALUES ('johndoe@example.com', 35789, 'John', 'Doe', 'N', 'password789', 'US12345678901234567890');

/*Maria Schmidt */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (780526, 'Deutschland', 'Schillerstrasse', 45, 10115, 'Berlin');
INSERT INTO Kunde(mail, adressID, vorname, nachname, newsletter, passwort, iban)
	VALUES ('mariaschmidt@example.com', 780526, 'Maria', 'Schmidt', 'Y', 'passwordABC', 'DE98765432109876543210');

/*Juan Perez */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (28550, 'Spanien', 'Calle Mayor', 67, 28001, 'Madrid');
INSERT INTO Kunde(mail, adressID, vorname, nachname, newsletter, passwort, iban)
	VALUES ('juanperez@example.com', 28550, 'Juan', 'Perez', 'Y', 'passwordXYZ', 'ES12345678901234567890');

/*Sophie Mann */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (25795, 'Frankreich', 'Rue de Rivoli', 24, 75001, 'Paris');
INSERT INTO Kunde(mail, adressID, vorname, nachname, newsletter, passwort, iban)
	VALUES ('sophiemann@example.com', 25795, 'Sophie', 'Mann', 'N', 'passwordDEF', 'FR09876543210987654321');

--Ferienwohnungen 
/*Casa del Mar*/
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (20059,'Spanien', 'Carrer de la Marina', 10, 10000, 'Barcelona');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (1, 20059, 'Casa del Mar', 4, 100.00, 50.20);

/*Casa Bella*/
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (29596, 'Italien', 'Via Roma', 15, 20121, 'Mailand');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (2, 29596, 'Casa Bella', 3, 80.50, 60.00);

--Le Charme
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (83308, 'Frankreich', 'Rue de la Paix', 20, 75002, 'Paris');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (3, 83308, 'Le Charme', 2, 65.75, 70.80);

--/Sonnenschein Villa/
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (12855, 'Griechenland', 'Agiou Konstantinou', 5, 54621, 'Thessaloniki');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (4, 12855, 'Sonnenschein Villa', 5, 120.00, 90.00);

/*Am Schottenring */
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (23065, 'Österreich', 'Am Schottenring', 30, 1010, 'Wien');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (5, 23065, 'Alpenblick Chalet', 6, 150.50, 110.25);

--/Otel de Madrid/
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (20289,'Spanien', 'Plaza de la Bella', 10, 10020, 'Madrid');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (6, 20289, 'Otel de Madrid', 3, 110.00, 51.20);
--/Avenida de Valencia/
INSERT INTO Adresse(adressID, landname, strasse, nr, plz, stadtname)
	VALUES (20232,'Spanien', 'Avenida de Valencia', 5, 10220, 'Valencia');
INSERT INTO Ferienwohnung(ferienwohnungID, adressID, ferienwohnung_name, zimmer, groesse, preis)
	VALUES (7, 20232, 'Avenida de Valencia', 4, 110.00, 65.20);

--Buchung
/* Max Mustermann Casa Bella */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (136732, 'maxmustermann@example.com', 1, 'N', '25.05.2024', '05.06.2024', '20.05.2024', 5, '20.05.2024', '20.05.2024', 450.50);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (25859,136732, '20.05.2024', 100);

/* Juan Perez Casa Bella */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (136562, 'juanperez@example.com', 1, 'Y', '05.01.2025', '07.01.2025', '30.04.2024', 5, '20.05.2024', '30.08.2024', 1000.50);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (25459,136562, '30.04.2024', 500.50);

/* Juan Perez Le Charme */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (25845, 'juanperez@example.com', 3, 'N', '15.02.2023', '20.02.2023', '10.01.2023', 1,'22.02.2023', '15.02.2023', 550.50);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (23791,25845, '15.02.2023', 100.50);

/* Maria Schmidt Sonnenschein Villa */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (3790208, 'mariaschmidt@example.com', 2, 'N', '22.07.2024', '24.07.2024', '21.05.2024', NULL, NULL, '20.05.2024', 750.50);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (69533, 3790208,'20.05.2024', 300);

/* Erika Musterfrau Casa del Mar */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458300, 'erikamusterfrau@example.com', 1, 'N', '01.07.2024', '10.07.2024', '01.06.2024', 5, '11.07.2024', '01.06.2024', 800.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32020, 458300, '01.06.2024', 200.00);

INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458350, 'erikamusterfrau@example.com', 1, 'N', '01.05.2024', '10.05.2024', '01.06.2024', 5, '11.07.2024', '01.06.2024', 800.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32040, 458350, '01.06.2024', 200.00);

/* John Doe Casa Bella */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458301, 'johndoe@example.com', 2, 'N', '05.06.2024', '15.06.2024', '01.05.2024', 4, '16.06.2024', '01.05.2024', 950.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32001, 458301, '01.05.2024', 250.00);

/* Sophie Mann Le Charme */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458302, 'sophiemann@example.com', 3, 'N', '10.08.2024', '20.08.2024', '01.07.2024', 1, '01.07.2024', '01.07.2024', 800.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32002, 458302, '01.07.2024', 200.00);

/* Sophie Mann Sonnenschein Villa */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458303, 'sophiemann@example.com', 4, 'N', '18.08.2024', '25.08.2024', '01.07.2024', NULL, NULL, '01.07.2024', 900.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32003, 458303, '01.07.2024', 250.00);

/* Max Mustermann Sonnenschein Villa */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458304, 'maxmustermann@example.com', 4, 'N', '15.07.2024', '25.07.2024', '01.05.2024', 5, '26.07.2024', '01.05.2024', 1050.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32004, 458304, '01.05.2024', 300.00);

/* Maria Schmidt Casa del Mar */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458305, 'mariaschmidt@example.com', 1, 'N', '12.07.2024', '18.07.2024', '01.05.2024', 4, '19.07.2024', '01.05.2024', 800.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32005, 458305, '01.05.2024', 200.00);

/* Juan Perez Le Charme */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458306, 'juanperez@example.com', 3, 'N', '01.08.2024', '10.08.2024', '01.06.2024', 1, '11.08.2024', '01.06.2024', 750.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32006, 458306, '01.06.2024', 200.00);

/* John Doe Otel de Madrid */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458307, 'johndoe@example.com', 6, 'N', '10.06.2024', '20.06.2024', '01.05.2024', 5, '21.06.2024', '01.05.2024', 750.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32007, 458307, '01.05.2024', 250.00);

/* Sophie Mann Otel de Madrid */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458308, 'sophiemann@example.com', 6, 'N', '05.07.2024', '15.07.2024', '01.06.2024', 2, '16.07.2024', '01.06.2024', 850.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32008, 458308, '01.06.2024', 300.00);

/* Erika Musterfrau Avenida de Valencia */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458309, 'erikamusterfrau@example.com', 7, 'N', '01.08.2024', '10.08.2024', '01.07.2024', 4, '11.08.2024', '01.07.2024', 900.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32009, 458309, '01.07.2024', 250.00);

/* Juan Perez Avenida de Valencia */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458310, 'juanperez@example.com', 7, 'N', '15.09.2024', '25.09.2024', '01.08.2024', 3, '26.09.2024', '01.08.2024', 1000.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32010, 458310, '01.08.2024', 300.00);

/* Max Mustermann Avenida de Valencia */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (452310, 'maxmustermann@example.com', 2, 'N', '15.10.2024', '25.10.2024', '01.08.2024', 5, '26.09.2024', '01.08.2024', 1000.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (32310, 452310, '01.08.2024', 300.00);

/* Sophie Mann Le Charme */
INSERT INTO Buchung(buchungsnummer, mail, ferienwohnungID, storniert, beginn, ende, buchungsdatum, sterne, bewertungsdatum, rechnungsdatum, betrag)
    VALUES (458332, 'sophiemann@example.com', 3, 'N', '10.09.2024', '20.09.2024', '01.07.2024', 1, '01.07.2024', '01.07.2024', 800.00);
INSERT INTO Anzahlung(anzahlungID, buchungsnummer, zahlungsdatum, betrag)
    VALUES (320222, 458332, '01.07.2024', 200.00);

--Bild 
INSERT INTO Bild(link, ferienwohnungID)
	VALUES('bild1/CasaDelMar.png', 1);
INSERT INTO Bild(link, ferienwohnungID)
	VALUES ('bild2/CasaDelMar.png',1);
INSERT INTO Bild(link, ferienwohnungID)
	VALUES('bild1/CasaBella.jpeg', 2);
INSERT INTO Bild(link, ferienwohnungID)
	VALUES('bild1/LeCharme.png', 3);
INSERT INTO Bild(link, ferienwohnungID)
	VALUES('bild1/SonnenscheinVilla.jpg', 4);
INSERT INTO Bild(link, ferienwohnungID)
	VALUES('bild1/AmSchottenring.jpg', 5);

--Austattungen
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('Küche');
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('Balkon');
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('WLAN');
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('Klimaanlage');
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('Fernseher');
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('Parkplatz');
INSERT INTO Ausstattung(ausstattungsname)
	VALUES('Sauna');

--beinhaltet
-- Einträge für Ferienwohnung 1
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (1, 'Küche');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (1, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (1, 'WLAN');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (1, 'Sauna');

-- Einträge für Ferienwohnung 2
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (2, 'Sauna');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (2, 'Küche');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (2, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (2, 'WLAN');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (2, 'Parkplatz');

-- Einträge für Ferienwohnung 3
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (3, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (3, 'WLAN');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (3, 'Klimaanlage');

-- Einträge für Ferienwohnung 4
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (4, 'Sauna');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (4, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (4, 'Parkplatz');

-- Einträge für Ferienwohnung 5
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (5, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (5, 'Sauna');

-- Einträge für Ferienwohnung 6
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (6, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (6, 'WLAN');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (6, 'Klimaanlage');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (6, 'Sauna');

-- Einträge für Ferienwohnung 7
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (7, 'Balkon');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (7, 'Klimaanlage');
INSERT INTO beinhaltet(ferienwohnungID, ausstattungsname) VALUES (7, 'Sauna');


--Touristenattraktion
INSERT INTO Touristenattraktion(touristenattraktionname, beschreibung) 
	VALUES('Aquarium', 'Ein faszinierender Ort.');

INSERT INTO Touristenattraktion(touristenattraktionname, beschreibung) 
	VALUES('Botanischer Garten', 'Schön.');

INSERT INTO Touristenattraktion(touristenattraktionname, beschreibung) 
	VALUES('Historisches Viertel', NULL);

INSERT INTO Touristenattraktion(touristenattraktionname, beschreibung) 
	VALUES('Zoo', 'Vielzahl von Tieren.');

--in der Nähe
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('1', 'Zoo', 15.56);
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('1', 'Botanischer Garten', 20.05);
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('2', 'Botanischer Garten', 1.05);
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('2', 'Historisches Viertel', 2.15);
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('3', 'Aquarium', 23);
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('4', 'Aquarium', 2.35);
INSERT INTO in_der_naehe(ferienwohnungID, touristenattraktionname, entfernung)
	VALUES ('5', 'Historisches Viertel', 266.67);


SELECT * FROM Ferienwohnung;
