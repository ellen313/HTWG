-- CREATE
CREATE TABLE Land (
    landname VARCHAR (50) NOT NULL,
    CONSTRAINT land_pk PRIMARY KEY (landname)
);

CREATE TABLE Adresse (
    adressID INT,
    landname VARCHAR (50) NOT NULL,
    nr CHAR (5) NOT NULL,
    strasse VARCHAR (50) NOT NULL,
    plz INT NOT NULL,
    stadtname VARCHAR (50) NOT NULL,
    CONSTRAINT adresse_pk PRIMARY KEY (adressID),
    CONSTRAINT adresse_fk FOREIGN KEY (landname) REFERENCES land(landname),
    CONSTRAINT adresse_plz CHECK (plz > 0)
);

CREATE TABLE Ferienwohnung (
    ferienwohnungID INT NOT NULL,
    adressID INT NOT NULL,
    ferienwohnungname VARCHAR (40) NOT NULL,
    zimmer INT NOT NULL CHECK (zimmer > 0),
    groesse NUMBER (10,2) NOT NULL CHECK (groesse > 0),
    preis NUMBER (10,2) NOT NULL CHECK (preis > 0),
    CONSTRAINT ferienwohnung_pk PRIMARY KEY (ferienwohnungID),
    CONSTRAINT ferienwohnung_fk FOREIGN KEY (adressID) REFERENCES adresse(adressID),
    CONSTRAINT ferienwohnungname_check CHECK (LENGTH(ferienwohnungname) > 2)
);

CREATE TABLE Kunde (
    mail VARCHAR (50) NOT NULL,
    adressID INT NOT NULL,
    vorname VARCHAR (50) NOT NULL,
    nachname VARCHAR (50) NOT NULL,
    newsletter CHAR (1) NOT NULL,
    passwort VARCHAR (20) NOT NULL,
    iban VARCHAR (34) NOT NULL,
    CONSTRAINT kunde_pk PRIMARY KEY (mail),
    CONSTRAINT kunde_fk FOREIGN KEY (adressID) REFERENCES adresse(adressID),
    CONSTRAINT vorname_check CHECK (LENGTH(vorname) > 2),
    CONSTRAINT newsletter_check CHECK (newsletter = '1' OR newsletter = '0'),
    CONSTRAINT nachname_check CHECK (LENGTH(nachname) > 2),
    CONSTRAINT iban_check CHECK (LENGTH(iban) < 35)
);

CREATE TABLE Buchung (
    buchungsnr INT,
    mail VARCHAR (50),
    ferienwohnungID INT,
    storniert CHAR (1) NOT NULL,
    stornodatum DATE,
    beginn DATE NOT NULL,
    ende DATE NOT NULL,
    buchungsdatum DATE NOT NULL,
    sterne INT,
    bewertungsdatum DATE,
    rechungsdatum DATE,
    betrag NUMBER (10,2) NOT NULL,
    CONSTRAINT buchung_pk PRIMARY KEY (buchungsnr),
    CONSTRAINT buchung_fk1 FOREIGN KEY (ferienwohnungID) REFERENCES ferienwohnung(ferienwohnungID),
    CONSTRAINT buchung_fk2 FOREIGN KEY (mail) REFERENCES kunde(mail),
    CONSTRAINT buchungbeginn CHECK (beginn < ende AND ende > beginn),
    CONSTRAINT bewertung_check CHECK (bewertungsdatum > ende),
    CONSTRAINT storno_check CHECK (stornodatum > buchungsdatum),
    CONSTRAINT storniert_check CHECK (storniert = '1' OR storniert = '0'),
    CONSTRAINT buchungsbetrag CHECK (betrag > 0),
    CONSTRAINT bewertungsterne CHECK (sterne >= 1 AND sterne <= 5)
);

CREATE TABLE Anzahlung (
    anzahlungsID INT,
    buchungsnr INT NOT NULL,
    zahlungsdatum DATE NOT NULL,
    betrag NUMBER (10,2) NOT NULL,
    CONSTRAINT anzahlung_pk PRIMARY KEY (anzahlungsID),
    CONSTRAINT anzahlung_fk FOREIGN KEY (buchungsnr) REFERENCES buchung(buchungsnr),
    CONSTRAINT anzahlungsbetrag CHECK (betrag > 0)
);

CREATE TABLE Austattung (
    austattungsname VARCHAR (30) NOT NULL,
    CONSTRAINT austattung_pk PRIMARY KEY (austattungsname)
);

CREATE TABLE Bild (
    link VARCHAR (30) NOT NULL,
    ferienwohnungID INT,
    CONSTRAINT bild_pk PRIMARY KEY (link),
    CONSTRAINT bild_fk FOREIGN KEY (ferienwohnungID) REFERENCES ferienwohnung(ferienwohnungID) ON DELETE CASCADE,
    CONSTRAINT link_format CHECK (REGEXP_LIKE(link, '^.*\.(jpg|jpeg|png)$'))
);

CREATE TABLE Touristenattraktion (
    touriattname VARCHAR (30) NOT NULL,
    beschreibung VARCHAR (100) NOT NULL,
    CONSTRAINT touristenattraktion_pk PRIMARY KEY (touriattname)
);

CREATE TABLE In_der_Naehe (
    ferienwohnungID INT,
    touriattname VARCHAR (30),
    entfernung NUMBER NOT NULL,
    CONSTRAINT in_der_naehe PRIMARY KEY (entfernung),
    CONSTRAINT in_der_naehe_fk1 FOREIGN KEY (ferienwohnungID) REFERENCES ferienwohnung(ferienwohnungID),
    CONSTRAINT in_der_naehe_fk2 FOREIGN KEY (touriattname) REFERENCES touristenattraktion(touriattname)
);

CREATE TABLE Beinhaltet (
    ferienwohnungID INT,
    austattungsname VARCHAR (30),
    CONSTRAINT beinhaltet_fk1 FOREIGN KEY (ferienwohnungID) REFERENCES ferienwohnung(ferienwohnungID),
    CONSTRAINT beinhaltet_fk2 FOREIGN KEY (austattungsname) REFERENCES austattung(austattungsname) ON DELETE CASCADE
);

--a)
CREATE TABLE stornierte_Buchungen (
    buchungsnr INT PRIMARY KEY,
    ferienwohnungID INT,
    storniert CHAR (1) NOT NULL,
    stornodatum DATE DEFAULT SYSDATE,
    CONSTRAINT stornierte_Buchungen_fk1 FOREIGN KEY (ferienwohnungID) REFERENCES ferienwohnung(ferienwohnungID),
    CONSTRAINT storniert_stornierte_Buchungen_check CHECK (storniert = '1')
);

/*CREATE OR REPLACE TRIGGER stornierte_Buchungen_Trigger
    BEFORE DELETE ON Buchung
    FOR EACH ROW
    begin
        INSERT INTO stornierte_Buchungen (
            buchungsnr, ferienwohnungID, storniert, stornodatum
        ) VALUES (
            :OLD.buchungsnr, :OLD.ferienwohnungID, '1', SYSDATE
        );
    END;
/

--DELETE FROM Buchung WHERE buchungsnr = 644890;*/

--b)
CREATE OR REPLACE VIEW Kundenstatistik AS 
SELECT 
	k.mail AS Kunden_ID, 
	COALESCE(COUNT(b.buchungsnr), 0) AS Anzahl_Buchungen, 
	COALESCE(SUM(CASE WHEN b.storniert = '1' THEN 1 ELSE 0 END), 0) AS Anzahl_Stornierungen,
	COALESCE(SUM(b.betrag), 0) As Summe_Zahlungen 
FROM
	Kunde k, Buchung b  --left join zw kunde und buchung
WHERE 
	k.mail = b.mail 
GROUP BY
	k.mail;

--c)
/*SELECT DISTINCT fw2.ferienwohnungname AS Empfehlung
FROM Buchung b1
JOIN Buchung b2 ON b1.ferienwohnungID = b2.ferienwohnungID
JOIN Buchung b3 ON b2.mail = b3.mail AND b3.sterne = 5
JOIN Ferienwohnung fw2 ON b3.ferienwohnungID = fw2.ferienwohnungID
WHERE b1.mail = 'K1_MAIL'
AND b1.sterne = 5
AND b2.mail != b1.mail
AND b2.sterne = 5;*/
/*SELECT DISTINCT b3.ferienwohnungID,
    (SELECT fw.ferienwohnung_name
      FROM Ferienwohnung fw
      WHERE fw.ferienwohnungID = b3.ferienwohnungID) AS ferienwohnung_name
FROM Buchung b1
JOIN Buchung b2 ON b1.ferienwohnungID = b2.ferienwohnungID
JOIN Buchung b3 ON b2.mail = b3.mail 
WHERE b2.mail = 'lara@example.com'
  AND b1.mail = 'bernd@example.com'
  AND b1.mail != b2.mail
  AND b1.sterne = 5
  AND b2.sterne = 5
  AND b3.sterne = 5
  AND b1.ferienwohnungID != b3.ferienwohnungID;

SELECT * FROM Buchung WHERE mail = 'bernd@example.com' AND sterne = 5;
SELECT * FROM Buchung WHERE mail = 'lara@example.com' AND sterne = 5;

SELECT * FROM Buchung WHERE mail = 'bernd@example.com';
SELECT * FROM Buchung WHERE mail = 'lara@example.com';*/

--d)
CREATE INDEX idx_ferienwohnung_id ON Ferienwohnung (ferienwohnungID);

CREATE INDEX idx_buchung_ferinwohnungid_beginn_ende ON Buchung (ferienwohnungID, beginn, ende, buchungsnr);

CREATE INDEX idx_adresse_adresse_adressid_landname ON Adresse (adressID, landname);

CREATE INDEX idx_beinhaltet_ferienwohnungid_ausstattungensname ON beinhaltet (ferienwohnungID, austattungsname);

CREATE INDEX idx_buchung_mail ON Buchung (mail);