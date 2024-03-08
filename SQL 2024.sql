select * from team;

select max(bib) from team;
select max(id) from team;

select * from heat_teams;

insert into team (age, bib, id, club_name, gender_class, group_name, participant_leg1name, participant_leg2name, team_name)
values (10, 112, 112, 'Bærums Verk Hauger IF', 'J', 'J10 år', 'Ane Nergården Lange', 'Sofia Bøthun-Hansen', 'Bærums Verk Hauger IF - Lag 1');

insert into heat_teams values (4, 112);
insert into heat_teams values (8, 112);

-- lag 96 må flyttes fra G13 til G11
select * from team where bib = 96;
select * from heat_teams where teams_id = 46;
update heat_teams set heats_heat_number = 27 where teams_id = 46;

select * from team where bib = 27;
select * from heat_teams where teams_id = 97;
delete from heat_teams where teams_id = 97;
delete from team where bib = 27;

-- avmelding av Dikemark IF - Ski - Lag 1
select * from team where bib = 100;
select * from heat_teams where teams_id = 50;
delete from heat_teams where teams_id = 50;
delete from team where bib = 100;