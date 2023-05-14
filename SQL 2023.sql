select * from team where (participant_leg1name = 'Navn Løs' or participant_leg2name = 'Navn Løs') order by club_name;

select age, bib, club_name, group_name, participant_leg1name, participant_leg2name from team where (participant_leg1name = 'Navn Løs' or participant_leg2name = 'Navn Løs') and club_name = 'Gui SK - Langrenn';

select count(*) from team;

select count(*) from participant;

select * from team;

select * from team where bib = 5;
update team set participant_leg2name = 'Mio Gowers Langelid' where bib = 5;

select * from team where club_name = 'Haslum IL - Ski';

select * from team where bib = 113;
update team set participant_leg2name = 'Marius Bagås' where bib = 113;

select * from team where bib = 109;
update team set participant_leg1name = 'Helene Westerheim Larsen' where bib = 109;


-- flytte Jenny fra lag 33 til lag 17
select * from team where bib = 33;
select * from team where bib = 17;
update team set participant_leg2name = 'Jenny Sørhus Voss' where bib = 17;
delete from heat_teams where teams_id = (select id from team where bib = 33);
-- lag 33 kan nå slettes, slettet via swagger

select * from team where bib = 160;
delete from heat_teams where teams_id = (select id from team where bib = 160);

select * from team where bib = 15;
update team set participant_leg2name = 'Navn Løs' where bib = 15;

select * from team where bib = 36;

select * from team where id = 184;

select * from heat;
select * from heat_teams;
insert into heat_teams values (2, 184);
insert into heat_teams values (8, 184);

select * from team where bib = 36;
update team set participant_leg1name = 'Johanne Paulsen Stein' where bib = 36;

select * from team where bib = 1001;


select * from team where bib = 53;
update team set participant_leg1name = 'Malla Von Krogh' where bib = 53;
delete from heat_teams where teams_id = (select id from team where bib = 58);


select * from team where bib = 103;

select * from team where bib = 100;
update team set participant_leg1name = 'Ola Heen' where bib = 100;

select * from participant where


select * from team where bib = 120;
update team set participant_leg1name = 'Ola Heen' where bib = 120;

select * from team where bib = 171;
update team set participant_leg1name = 'Ola Heen' where bib = 120;

select * from team where bib = 102;

select * from team where bib = 136;
update team set participant_leg2name = 'Johan Berntsen' where bib = 136;

select * from team where bib = 136;
update team set participant_leg2name = 'Johan Berntsen' where bib = 136;

select * from team where bib = 183;


select * from heat_teams where heats_heat_number =51;
insert into heat_teams values(51, (select id from team where bib = 158));
insert into heat_teams values(51, (select id from team where bib = 166));
insert into heat_teams values(51, (select id from team where bib = 169));
insert into heat_teams values(51, (select id from team where bib = 161));
insert into heat_teams values(51, (select id from team where bib = 165));

insert into heat_teams values(52, (select id from team where bib = 167));
insert into heat_teams values(52, (select id from team where bib = 162));
insert into heat_teams values(52, (select id from team where bib = 157));
insert into heat_teams values(52, (select id from team where bib = 163));
insert into heat_teams values(52, (select id from team where bib = 168));
insert into heat_teams values(52, (select id from team where bib = 164));

insert into heat_teams values(53, (select id from team where bib = 180));
insert into heat_teams values(53, (select id from team where bib = 174));
insert into heat_teams values(53, (select id from team where bib = 179));
insert into heat_teams values(53, (select id from team where bib = 182));
insert into heat_teams values(53, (select id from team where bib = 176));
insert into heat_teams values(53, (select id from team where bib = 178));

insert into heat_teams values(54, (select id from team where bib = 173));
insert into heat_teams values(54, (select id from team where bib = 172));
insert into heat_teams values(54, (select id from team where bib = 177));
insert into heat_teams values(54, (select id from team where bib = 183));
insert into heat_teams values(54, (select id from team where bib = 175));
insert into heat_teams values(54, (select id from team where bib = 170));
insert into heat_teams values(54, (select id from team where bib = 171));
insert into heat_teams values(54, (select id from team where bib = 181));


delete from heat_teams where heats_heat_number = 51 and teams_id = 158;
insert into heat_teams values(51, 1);


insert into heat_teams values(52, );

select * from heat_result where heat_heat_number = 51;
delete from heat_result where heat_heat_number = 51;

select count(*) from team;
select count(*)/2 from participant;