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