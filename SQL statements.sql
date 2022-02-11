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
update participant set first_name = 'Josefine', last_name = 'Opheim' where id = 302;
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
