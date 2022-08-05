CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

create table "User"(
    userId text,
    name varchar(25) null,
    email text,
    about varchar(255) null,
    profileImage text default 'https://crossworkingbucket.s3.eu-west-2.amazonaws.com/profile_default_picture.png',
    primary key(userId),
    unique(email),
    constraint check_email check (email ~* '^[A-Za-z0-9._+%-]+@[A-Za-z0-9.-]+[.][A-Za-z]+$')
);

create table Idea(
    ideaId uuid default uuid_generate_v4(),
    userId text,
    title varchar(25),
    smallDescription varchar(50),
    description text,
    date date default current_date,
    unique (userId, title),
    primary key (ideaId),
    foreign key (userId) references "User"(userId) on delete cascade
);

create table Category(
     categoryId uuid default uuid_generate_v4(),
     name varchar(25),
     primary key (categoryId)
);

create table Skill(
    skillId uuid default uuid_generate_v4(),
    name varchar(25),
    categoryId uuid,
    primary key(skillId),
    unique (name),
    foreign key (categoryId) references Category(categoryId)
);

create table UserSkill(
    userId text,
    skillId uuid,
    about varchar(255) null,
    primary key (userId, skillId),
    foreign key (skillId) references Skill(skillId),
    foreign key (userId) references "User"(userId) on delete cascade
);

create table IdeaSkill(
    ideaId uuid,
    skillId uuid,
    about varchar(255) null,
    primary key (ideaId, skillId),
    foreign key (skillId) references Skill(skillId),
    foreign key (ideaId) references Idea(ideaId) on delete cascade
);

create table Candidate(
    userId text,
    ideaId uuid,
    status varchar(10) default 'pending',
    createdDate date default current_date,
    lastUpdateDate date default current_date,
    primary key (userId, ideaId),
    foreign key (userId) references "User"(userId) on delete cascade,
    foreign key (ideaId) references Idea(ideaId) on delete cascade,
    constraint check_status check (status = 'pending' or status = 'accepted' or status = 'declined')
);

------------------------------------------------------------------

create view UserSkillAdd as
    select  us.userId, s.name as skillName, us.about, c.categoryId as categoryId
        from userskill as us join skill as s on us.skillId=s.skillId join category as c on s.categoryId=c.categoryId;

create view IdeaSkillAdd as
select  isl.ideaId, s.name as skillName, isl.about, c.categoryId as categoryId
from ideaskill as isl join skill as s on isl.skillId=s.skillId join category as c on s.categoryId=c.categoryId;

------------------------------------------------------------------

create function add_user_skill(givenUserId text, givenName varchar(25), givenAbout varchar(255), givenCategoryId uuid)
returns table(
    skillId uuid,
    skillName varchar(25),
    skillAbout varchar(255),
    categoryName varchar(25)
)
as $$
    declare skillCount integer;
    declare skillId UUID;
    declare categoryName varchar(25);
begin
        skillCount := (select count(*) from userskill where UserSkill.userId = givenUserId);
        if (skillCount >= 10)
        then
            raise exception using message ='max skills reached', errcode ='80000';
        else
            if not exists(select * from skill where Skill.name = givenName)
            then
                insert into skill(name, categoryId) values (givenName, givenCategoryId);
            end if;

            skillId := (select Skill.skillid from skill where Skill.name = givenName);
            insert into userskill(userId, skillId, about) values (givenUserId, skillId, givenAbout);

            categoryName := (select name from Category where categoryId = givenCategoryId);

            return query select skillId, givenName, givenAbout, categoryName;
        end if;
end
$$ language plpgsql;

------------------------------------------------------------------

create function add_idea_skill(givenIdeaId uuid, givenName varchar(25), givenAbout varchar(255), givenCategoryId uuid)
returns table(
    skillId uuid,
    skillName varchar(25),
    skillAbout varchar(255),
    categoryName varchar(25)
)
as $$
    declare skillCount integer;
    declare skillId UUID;
    declare categoryName varchar(25);
begin
    skillCount := (select count(*) from IdeaSkill where IdeaSkill.ideaId = givenIdeaId);
    if (skillCount >= 10)
    then
        raise exception using message ='max skills reached', errcode ='80000';
    else
        if not exists(select * from skill where Skill.name = givenName)
        then
            insert into skill(name, categoryId) values (givenName, givenCategoryId);
        end if;

        skillId := (select Skill.skillid from skill where Skill.name = givenName);
        insert into ideaskill(ideaId, skillId, about) values (givenIdeaId, skillId, givenAbout);

        categoryName := (select name from Category where categoryId = givenCategoryId);

        return query select skillId, givenName, givenAbout, categoryName;
    end if;
end
$$ language plpgsql;

-------------------------------------------------------------------

create function check_self_candidate() returns trigger as
$$
begin
    if exists(select * from idea where ideaid= new.ideaId and Idea.userId=new.userId)
    then
        raise exception using message ='You can not self candidate', errcode ='80001';
    end if;
    return new;
end
$$ language plpgsql;

create trigger check_self_candidate
    before insert
    on Candidate
    for each row
execute procedure check_self_candidate();

------------------------------------------------------------------
