drop view if exists ideaskilladd;
drop view if exists userskilladd;

drop table if exists Candidate;
drop table if exists IdeaSkill;
drop table if exists UserSkill;
drop table if exists Skill;
drop table if exists Category;
drop table if exists Idea;
drop table if exists "User";

drop function if exists add_idea_skill(uuid, varchar, varchar, uuid);
drop function if exists add_user_skill(text, varchar, varchar, uuid);
drop function if exists check_self_candidate();
