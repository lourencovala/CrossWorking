truncate table test.public."User" CASCADE;
truncate table test.public.category CASCADE;

insert into test.public."User"(userId, name, email, about, profileImage)
values ('g2Nb9AA541fRNGGk8oE8yh42qJn1', 'User 1', 'a@gmail.com', 'I am User 1',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/3/3f/NYCS-bull-trans-1.svg/1200px-NYCS-bull-trans-1.svg.png'),
       ('Mz3IB4kpBugVffdIdC8SvaKlc0u1', 'User 2', 'b@gmail.com', 'I am User 2',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/6/61/NYCS-bull-trans-2.svg/2048px-NYCS-bull-trans-2.svg.png'),
       ('j9rSb9GUagWFv5rsgmRviZYFYlm1', 'User 3', 'c@gmail.com', 'I am User 3',
        'https://upload.wikimedia.org/wikipedia/commons/thumb/2/25/NYCS-bull-trans-3.svg/1200px-NYCS-bull-trans-3.svg.png');

insert into test.public.idea(ideaId, userId, title, smalldescription, description)
values ('c5e36241-e105-4d6c-9884-da5105473c48', 'g2Nb9AA541fRNGGk8oE8yh42qJn1', 'Idea 1', 'small text',
        'Very large text. Contains a lot of information. Occupies a lot of lines ideally. A lot of text here. More text. Maybe some more. More wont hurt. This is for testing purposes.'),
       ('9d21f1c9-2836-4755-a8b8-1b7cc767609e', 'Mz3IB4kpBugVffdIdC8SvaKlc0u1', 'Idea 2', 'small text',
        'Very large text. Contains a lot of information. Occupies a lot of lines ideally. A lot of text here. More text. Maybe some more. More wont hurt. This is for testing purposes.'),
       ('454f5b61-d931-45bf-ba2c-1d8f16f621af', 'j9rSb9GUagWFv5rsgmRviZYFYlm1', 'Idea 3', 'small text',
        'Very large text. Contains a lot of information. Occupies a lot of lines ideally. A lot of text here. More text. Maybe some more. More wont hurt. This is for testing purposes.');

insert into test.public.Category(categoryid, name)
values ('bd1fde89-4ccd-4c51-a4a4-e39dc40009dd', 'Technology'),
       ('313ad4b1-0d2d-407e-8a6c-7bc32c91c3fe', 'Cooking'),
       ('fbf8df2b-254d-48ff-899a-ca6340326ea9', 'Sports'),
       ('42534b44-b0da-45dd-ae99-6eea504f9579', 'Crafts'),
       ('0d9ee4c2-990e-4d46-ba9a-2b2dd3903ae4', 'Arts'),
       ('cbaa5a3d-5d4f-432e-8fb8-426918c1df90', 'Tools'),
       ('413b00be-1dd5-46fa-a62a-150ce0589669', 'Other');

insert into test.public.skill(skillid, name, categoryid)
values ('91990805-003b-4103-8936-1ba7f4ab19d4', 'Kotlin', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('e2fe0c34-e2bc-48aa-b332-5ee3b489bbba', 'Java', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('7f860930-84ee-468b-8ba4-e88fb032e4da', 'JavaScript', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('337eebf5-b099-4fb9-9efd-5d3d8d97868f', 'Flutter', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('4ab2ecd3-22ba-44e4-bc8a-f18763fc5834', 'React Native', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('15d355a4-8f48-4fc1-a90f-a82656179368', 'Sql', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('030a8097-878c-47b6-95b1-60429a61c7bd', 'C#', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('d9c8819b-9290-4f50-b9bc-5d45b1d656d7', 'CSS', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('b18d596a-fa4e-4e4e-9251-68deba4160bf', 'HTML', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('e6503132-8ca0-4d38-b9b4-7d30c1123d56', 'Android', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('4d447a36-7408-4cb3-984f-4e2ae19e9e9a', 'iOS', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('c63c758d-14be-473a-8ead-8d5e86dc3319', 'Linux', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd'),
       ('d24e69f0-de73-49fa-834b-e84a9dd6969f', 'C++', 'bd1fde89-4ccd-4c51-a4a4-e39dc40009dd');


insert into test.public.userskill(userid, skillid, about)
values ('g2Nb9AA541fRNGGk8oE8yh42qJn1', '91990805-003b-4103-8936-1ba7f4ab19d4',
        'One of my main programming languages.'),
       ('g2Nb9AA541fRNGGk8oE8yh42qJn1', 'e6503132-8ca0-4d38-b9b4-7d30c1123d56',
        'Been developing Android for 2 years. Mainly in Kotlin'),
       ('g2Nb9AA541fRNGGk8oE8yh42qJn1', '7f860930-84ee-468b-8ba4-e88fb032e4da',
        'Used JavaScript a lot for web development.');

insert into test.public.ideaskill(ideaid, skillid, about)
values ('9d21f1c9-2836-4755-a8b8-1b7cc767609e', '7f860930-84ee-468b-8ba4-e88fb032e4da',
        'Needs to be efficient with JavaScript');

insert into test.public.candidate(userid, ideaid, status)
values ('g2Nb9AA541fRNGGk8oE8yh42qJn1', '9d21f1c9-2836-4755-a8b8-1b7cc767609e', 'pending'),
       ('j9rSb9GUagWFv5rsgmRviZYFYlm1', '9d21f1c9-2836-4755-a8b8-1b7cc767609e', 'accepted');
