CREATE TABLE IF NOT EXISTS public.author
(
    id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    first_name character varying(255) COLLATE pg_catalog."default",
    last_name character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT author_pkey PRIMARY KEY (id)
);

CREATE TABLE IF NOT EXISTS public.book
(
    isbn character varying(255) COLLATE pg_catalog."default" NOT NULL,
    annotation text COLLATE pg_catalog."default",
    book_url character varying(255) COLLATE pg_catalog."default",
    count integer NOT NULL,
    cover character varying(255) COLLATE pg_catalog."default",
    name character varying(255) COLLATE pg_catalog."default",
    price double precision NOT NULL,
    publishing_house character varying(255) COLLATE pg_catalog."default",
    visited integer NOT NULL,
    year integer NOT NULL,
    CONSTRAINT book_pkey PRIMARY KEY (isbn)
);

CREATE TABLE IF NOT EXISTS public.book_author
(
    book_isbn character varying(255) COLLATE pg_catalog."default" NOT NULL,
    author_id character varying(255) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT book_author_pkey PRIMARY KEY (book_isbn, author_id),
    CONSTRAINT fkbjqhp85wjv8vpr0beygh6jsgo FOREIGN KEY (author_id)
        REFERENCES public.author (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fklxhwaiinp1xunnqsvnoirba4j FOREIGN KEY (book_isbn)
        REFERENCES public.book (isbn) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

CREATE TABLE IF NOT EXISTS public.book_genre
(
    isbn character varying(255) COLLATE pg_catalog."default" NOT NULL,
    genre character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT fk90wk4nweaw0jvxo1o9edv7s9h FOREIGN KEY (isbn)
        REFERENCES public.book (isbn) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
);

INSERT INTO public.author(
    id, first_name, last_name)
VALUES ('id-1', 'Leigh','Bardugo');
INSERT INTO public.author(
    id, first_name, last_name)
VALUES ('id-2', 'Frank','Herbert');
INSERT INTO public.author(
    id, first_name, last_name)
VALUES ('id-3', 'Robert','Martin');


INSERT INTO public.book(
    isbn, annotation,  count, cover, name, price, publishing_house, visited, year)
VALUES ('isbn-1', 'Dune is set in the distant future amidst a feudal interstellar society in which various noble houses control planetary fiefs. It tells the story of young Paul Atreides, whose family accepts the stewardship of the planet Arrakis. While the planet is an inhospitable and sparsely populated desert wasteland, it is the only source of melange, or "spice", a drug that extends life and enhances mental abilities. Melange is also necessary for space navigation, which requires a kind of multidimensional awareness and foresight that only the drug provides. As melange can only be produced on Arrakis, control of the planet is a coveted and dangerous undertaking. The story explores the multilayered interactions of politics, religion, ecology, technology, and human emotion, as the factions of the empire confront each other in a struggle for the control of Arrakis and its spice',
        200, 'dune.png', 'Dune', 500, 'Chilton Books', 15, 1965);
INSERT INTO public.book(
    isbn, annotation,  count, cover, name, price, publishing_house, visited, year)
VALUES ('isbn-2',
        'Wealthy merchant Jan Van Eck divulges the results of Hoedes experiment to 17-year-old criminal prodigy Kaz Brekker and tasks him with rescuing its inventor, Bo Yul-Bayur, from the Ice Court, an unbreachable military stronghold in Fjerda, and prevent the drugs existence from being exposed to the world. Kaz agrees for a hefty price and starts recruiting a crew: Inej Ghafa, his right-hand spy he had saved from a pleasure house called the Menagerie two years before; Nina Zenik, a Grisha Heartrender, who joins upon learning of his intention to free and employ Matthias Helvar, a former Fjerdan drüskelle (Grisha-hunter) detained at Hellgate Prison because of Nina; and Jesper Fahey, a Zemeni sharpshooter with a gambling addiction. Together, they break Matthias out of prison, who agrees to help in exchange for a pardon that would enable his reinstatement as a drüskelle. Kaz also enlists Wylan Van Eck, Jan Van Ecks runaway son, as a demolitions expert and leverage if Van Eck reneges on their deal. As they are about to sail from Ketterdam, the crew repels an ambush by rival gangs; after torturing a gangster, Kaz learns that gang leader Pekka Rollins, the man responsible for his brother Jordies death, is also after the scientist. Kaz explains his rescue plan to the crew: they will enter the Ice Court as prisoners, cross to the embassy sector through the roof and disguise themselves as foreign dignitaries during a festival. After finding and freeing Yul-Bayur from the White Island, on the inner ring, they will exit from the embassy sector.'
           , 200, 'six_of_crows.png', 'Six of crows', 400, 'Henry Holt and Co.', 15, 2015);
INSERT INTO public.book(
    isbn, annotation,  count, cover, name, price, publishing_house, visited, year)
VALUES ('isbn-3',
        'Noted software expert Robert C. Martin, presents a revolutionary paradigm with Clean Code: A Handbook of Agile Software Craftsmanship. Martin, who has helped bring agile principles from a practitioner’s point of view to tens of thousands of programmers, has teamed up with his colleagues from Object Mentor to distill their best agile practice of cleaning code “on the fly” into a book that will instill within you the values of software craftsman, and make you a better programmer―but only if you work at it.'
           , 200, 'clean_code.png', 'Clean code', 800, 'RockCityBooks', 15, 2005);


INSERT INTO public.book_author(
    book_isbn, author_id)
VALUES ('isbn-1', 'id-2');

INSERT INTO public.book_author(
    book_isbn, author_id)
VALUES ('isbn-2', 'id-1');

INSERT INTO public.book_author(
    book_isbn, author_id)
VALUES ('isbn-3', 'id-3');



INSERT INTO public.book_genre(
    isbn, genre)
VALUES ('isbn-1', 'FANTASY');
INSERT INTO public.book_genre(
    isbn, genre)
VALUES ('isbn-1', 'ADVENTURE');
INSERT INTO public.book_genre(
    isbn, genre)
VALUES ('isbn-3', 'DEVELOPMENT');
INSERT INTO public.book_genre(
    isbn, genre)
VALUES ('isbn-3', 'GUIDE');
INSERT INTO public.book_genre(
    isbn, genre)
VALUES ('isbn-2', 'THRILLER');
INSERT INTO public.book_genre(
    isbn, genre)
VALUES ('isbn-2', 'FANTASY');