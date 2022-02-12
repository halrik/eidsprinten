select * from team where (participant_leg1name = 'Navn Løs' or participant_leg2name = 'Navn Løs') order by club_name;

select * from team where bib = 167;
select * from participant where id = 293;
update participant set first_name = 'Jonas', last_name = 'Walle' where id = 293;
update team set participant_leg1name = 'Jonas Walle' where bib = 167;

select * from team where bib = 72;
select * from participant where id = 8;
update participant set first_name = 'Ola Jacobsen', last_name = 'Myklatun' where id = 8;
update team set participant_leg1name = 'Ola Jacobsen Myklatun' where bib = 72;

select * from team where bib = 167;
select * from participant where id = 294;
update participant set first_name = 'Navn', last_name = 'Løs' where id = 294;
update team set participant_leg2name = 'Navn Løs' where bib = 167;

select * from team where bib = 185;
select * from participant where id = 302;
update team set participant_leg2name = 'Josefine Opheim' where bib = 185;

select * from team where bib = 168;
select * from participant where id = 296;
update participant set first_name = 'Navn', last_name = 'Løs' where id = 296;
update team set participant_leg1name = 'Navn Løs' where bib = 168;

-- lag 140 (Alma og Celine Myhr) slettet

-- Klasse: G10
-- Lag: Holmen IF 4
-- Navn på utøver som mangler: Hao Zheng
select * from team where bib = 83;
select * from participant where id = 16;
update participant set first_name = 'Hao', last_name = 'Zheng' where id = 16;
update team set participant_leg2name = 'Hao Zheng' where bib = 83;

/*
Det har vært et frafall pluss litt misforståelser rundt hvem som skal være på lag på søndag. Er det mulig å endre på dette? Gjelder J 9 år og G 9 år fra Holmen.

Riktige lag skal være:

Selma Løvstad og Marie Roald Fosshaug (ny)
Matilde Storm Larsen og Edda Moger Nethaug (ny)
Mari Vetrhus og Olivie Fiksdal Haukvik
Arne Sølvberg Mossige og Matthew Tze Hong Guay
Håkon Blom-Martin og Sverre Kvåle Platou
Brede Landsgård og Eva Klæboe Nytvedt

*/

-- Selma Løvstad og Marie Roald Fosshaug (ny)
-- var J 9 år	Holmen IF 1	59	Eva Klæboe Nytvedt	Selma Løvstad
-- erstatte Agnes Leren Myhre (238) med Marie Roald Fosshaug
select * from participant where id = 238;
update participant set first_name = 'Marie Roald', last_name = 'Fosshaug' where id = 238;
-- erstatte Eva med Marie
select * from team where bib = 59;
update team set participant_leg1id = 238, participant_leg1name = 'Marie Roald Fosshaug' where bib = 59;

-- Matilde Storm Larsen og Edda Moger Nethaug (ny)
-- var J 9 år	Holmen IF 3	61	Matilde Storm Larsen	Mari Vetrhus (236)
-- erstatte Brede Landsgård (103) i team 41 med Edda Moger Nethaug
select * from participant where id = 103;
update participant set group_name = 'J 9 år', first_name = 'Edda Moger', last_name = 'NetHaug' where id = 103;
-- erstatte Mari Vetrhus med Edda Moger NetHaug (103) i lag 61
select * from team where bib = 61;
update team set participant_leg2id = 103, participant_leg2name = 'Edda Moger Nethaug' where bib = 61;

-- Mari Vetrhus og Olivie Fiksdal Haukvik
-- var J 9 år	Holmen IF 4	62	Olivie Fiksdal Haukvik	Agnes Leren Myhre (utgår)
-- erstatte Agnes med Mari (236)
select * from team where bib = 62;
select * from participant where id = 236;
update team set participant_leg2id = 236, participant_leg2name = 'Mari Vetrhus' where bib = 62;

-- Arne Sølvberg Mossige og Matthew Tze Hong Guay
-- G 9 år	Holmen IF 1	39	Arne Sølvberg Mossige	Matthew Tze Hong Guay
-- dette laget er ok
select * from team where bib = 39;

-- Håkon Blom-Martin og Sverre Kvåle Platou
-- var G 9 år	Holmen IF 2	40	Brede Landsgård	Håkon Blom-Martin
-- erstatte Brede med Sverre (105)
select * from team where bib = 40;
select * from participant where id = 104;
update team set participant_leg1id = 105, participant_leg1name = 'Sverre Kvåle Platou' where bib = 40;

-- Brede Landsgård og Eva Klæboe Nytvedt
-- var G 9 år	Holmen IF 3	41	Sverre Kvåle Platou	Brede Landsgård (105)
-- erstatte Sverre med Eva
select * from participant where first_name = 'Eva Klæboe';
select * from team where bib = 41;
update team set participant_leg1id = 232, participant_leg1name = 'Eva Klæboe Nytvedt' where bib = 41;

select * from team where bib in (59, 61, 62, 39, 40, 41);


-- J 9 år	IL Jardar 1	63	Live Prokosch Gjerding	Agnes Sæth Lille - endre til 8 år (var Felles 7 år)
select * from team where bib = 63;
select * from participant where id in (243, 244);
update team set age = 8, group_name = 'J 8 år' where bib = 63;
update participant set age = 8, group_name = 'J 8 år' where id in (243, 244);

-- slå sammen lag 167 og 168
select * from team where bib in (167, 168);
select * from participant where id in (295, 293);
update team set participant_leg2id = 295, participant_leg2name = 'Mathias Nøst-Hegge' where bib = 167;
update team set participant_leg1id = 294, participant_leg1name = 'Navnløs' where bib = 168;
-- lag 168 kan nå slettes

-- slå sammen lag, William og Magnus har trukket seg
-- G 11 år	Gui SK 2	129	Sindre Løvstad Rist	William Paul Wesche
-- G 11 år	Gui SK 1	128	Ludvik Berge-Lunden	Magnus Lundsett Fredriksen
select * from team where bib in (128, 129);
update team set participant_leg2id = 355, participant_leg2name = 'Sindre Løvstad Rist' where bib = 128;
-- lag 129 kan nå slettes


/*
Oppdateringer fra Ole Asbjørn
1. Linnea (erstatter Semine) - Frida
2. Maria Elise (erstatter Stiene) - Tiril
3. Thomas (G13) går inn på lag med Wilhelm (mix lag G14)
4. Maria Elise - Ylva (uendret)
5. Edvard (erstatter Erlend) - Erik
6. Tindre - Karl Markus (ny kombo)
7. Fredrik - Nicolas (uendret)
8. Laget med Linnea og Martine utgår (bib 220)
9. Laget med Edvard og Tindre utgår (bib 194)?
*/


-- 1. 	Linnea Løvstad Rist (erstatter Semine) - Frida
-- var J 14 år	Gui SK 1	221	Semina Haavaag Hakonsen	Frida Stryken Fundingsrud
select * from team where participant_leg1id = 319 or participant_leg2id = 319;
select * from participant where id = 319;
update participant set first_name = 'Linnea Løvstad', last_name = 'Rist' where id = 319;
update team set participant_leg1name = 'Linnea Løvstad Rist' where bib = 221;

-- 2. Maria Elise (erstatter Stiene) - Tiril - setter participant 1 Navn Løs enn så lenge
-- var J 14 år	Gui SK 3	219	Stiene Mie van der Velde	Tiril Windju Christianssen
select * from team where bib = 219;
select * from participant where id = 315;
update participant set first_name = 'Navn', last_name = 'Løs' where id = 315;
update team set participant_leg1name = 'Navn Løs' where bib = 219;

-- 3. Thomas (G13) går inn på lag med Wilhelm (mix lag G14)
-- var G 14 år	Gui SK 1	212	Wilhelm Kollerud	Navn Løs
select * from team where bib = 212;
select * from participant where id = 390;
update participant set first_name = 'Thomas Peter', last_name = 'Ennals', age = 13  where id = 390;
update team set participant_leg2name = 'Thomas Peter Ennals' where bib = 212;

-- 5. Edvard (erstatter Erlend) - Erik
-- G 13 år	Gui SK 1	193	Erlend Lundslett Fredriksen	Erik Stenersen Øksby
-- G 13 år	Gui SK 2	194	Edvard Lunde	Tindre Bjørholt
select * from team where bib in (193, 194);
update team set participant_leg2id = 305, participant_leg2name = 'Edvard Lunde' where bib = 193;

-- 6. Tindre - Karl Markus (ny kombo)
-- G 13 år	Gui SK 3	195	Thomas Peter Ennals	Karl Markus Lien
-- G 13 år	Gui SK 2	194	Edvard Lunde	Tindre Bjørholt
select * from team where bib in (194, 195);
update team set participant_leg1id = 306, participant_leg1name = 'Tindre Bjørholt' where bib = 195;

/*
J 13 år	Gui SK 1	206	Maria Elise Solberg	Ylva Hodneland Hansen

G 13 år	Gui SK 1	193	Erlend Lundslett Fredriksen	Erik Stenersen Øksby
G 13 år	Gui SK 2	194	Edvard Lunde	Tindre Bjørholt
G 13 år	Gui SK 3	195	Thomas Peter Ennals	Karl Markus Lien
G 13 år	Gui SK 4	196	Fredrik Myklebust ebbesen	Nikolas Ryen

J 14 år	Gui SK 3	219	Stiene Mie van der Velde	Tiril Windju Christianssen
J 14 år	Gui SK 2	220	Linnea Løvstad Rist	Martine Vannebo
J 14 år	Gui SK 1	221	Semina Haavaag Hakonsen	Frida Stryken Fundingsrud
 */

/*
J14: Linnea - Frida - Gui SK 1 - ok
J14: Tiril - Maria Elise (med forbehold om godkjenning fra rennledelsen) - Gui SK 2 - ok
G14: Thomas - Wilhelm (mixlag Drøbak/Gui SK 1) - ok
J13: Maria Elise - Ylva - ok
G13: Edvard - Erik
G13: Fredrik - Nicolas - ok
G13: Tindre - Karl Markus - ok
 */

select group_name, bib, participant_leg1name, participant_leg2name, team_name from team where club_name = 'Gui SK - Langrenn' and age in (13,14);
select group_name, bib, participant_leg1name, participant_leg2name, team_name from team where club_name = 'Gui SK - Langrenn' and group_name = 'G 14 år';

/*
Pga covid har vi fått noen endringer i lapoppstilling for Lommedalen IL 2010 og 2011.

Vi stiller følgende 4 lag / ett mindre enn påmeldt.

2010 gutter: Elias Molund og Simon Beck-Vehre
2010 jenter: Mia Seereeram Østfold og Sky Raistrick
2011 jenter: Ida Børsheim Lefdal og Matthea Myrstad
2011 gutter: Max Alexander Nybråten og Sebastian Fuglesang -- registrere nytt lag med bib 186

377 Eirik Hunsbedt Auran

var følgende lag:

G 12 år	Lommedalen IL 1	169	Elias Molund	Mia Seereeram Østland -- bytte med Simon Bech-Vehre
G 12 år	Lommedalen IL 2	170	Eirik Hunsbedt Auran	Simon Bech-Vehre -- slette og legge til nytt lag med G 11 år Max Alexander Nybråten og Sebastian Fuglesang
J 11 år	Lommedalen IL 1	144	Ida Børsheim Lefdahl	Mathea Myrstad -- ingen endring
J 12 år	Lommedalen IL 1	186	Maja Weber Eriksen	Sky Raistrick -- endres til J 12 år med Mia Seereeram Østfold og Sky Raistrick
 */

select * from team where club_name = 'Lommedalen IL - Langrenn' and age in (11,12);
select * from participant where id = 386;

select * from team where bib = 186


/*
 Hei,

Grunnet Covid-smitte så er vi nødt til å bytte ut en utøver i J10
Heat 23
Startnr 110
Start kl 12:00
Deltakere: Thelma Valderhaug og Sina Faye-Lund Solberg

Hilsen Benita Lundgren
Trener BVH langrenn
Sendt fra min iPhone
 */

select * from team where bib = 110;
update participant set first_name = 'Thelma', last_name = 'Valderhaug' where id = 150;
update team set participant_leg1name = 'Thelma Valderhaug' where bib = 110;

select * from heat_teams where heats_heat_number in (51,52);
select * from team where bib in (206,196); -- 155 flyttes til heat 52, 157 flyttes til heat 51
update heat_teams set teams_id = 157 where heats_heat_number = 51 and teams_id = 155;
update heat_teams set teams_id = 155 where heats_heat_number = 52 and teams_id = 157;

select * from team where bib = 50;
select * from participant where id = 121;

select * from team where bib = 186;


-- J 8 år	Gui SK 2	22	Emilie Aas	Tiril Kolderup-Steen
-- Tiril utgår
-- erstattes med Eline Walle Leknes
-- endre til J 8 år
select * from team where bib = 22;
select * from participant where id = 266;

-- G 12 år	Gui SK 8	167	Jonas Walle	Mathias Nøst-Hegge
-- G 12 år	Gui SK 1	160	Oliver Rognmo-Hodge	Sigurd Bertheussen Brinch
-- Jonas Walle og Oliver Rognmo-Hodge utgår
-- erstatt Oliver med Mathias på lag 160
-- slette lag 167
select * from team where bib = 160;
select * from participant where id = 279;