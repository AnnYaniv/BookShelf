--
-- TOC entry 3398 (class 0 OID 24862)
-- Dependencies: 215
-- Data for Name: invoice; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.invoice VALUES ('2e94da1f-8155-4880-bdb7-04b4f15aa8ea', '2023-05-22 20:56:13.316715', 'CREATING', '3fac42a0-826b-4cf4-904c-c3809049032a');
INSERT INTO public.invoice VALUES ('f3426e2e-bd68-477c-b6ad-483ca7c49bb3', '2023-05-23 19:01:43.856468', 'CREATING', '3fac42a0-826b-4cf4-904c-c3809049032a');
INSERT INTO public.invoice VALUES ('f9f6a753-e042-4572-a4ea-8c09a228aefd', '2023-05-26 03:42:23.87244', 'CREATING', '3fac42a0-826b-4cf4-904c-c3809049032a');
INSERT INTO public.invoice VALUES ('eb7b5629-9add-4c42-b43b-007b3e0392d4', '2023-05-26 03:42:56.72949', 'SHIPMENT', '9677494d-7360-4588-9c54-f79b96db3635');
INSERT INTO public.invoice VALUES ('b8d2055d-33ad-43e4-a0a9-431268393fdc', '2023-05-26 03:49:23.426903', 'CREATING', '8102c104-8280-40c3-8b4d-3628e687b3c4');
INSERT INTO public.invoice VALUES ('76d7edb6-d411-46f3-88e9-f7c1e3f8a75d', '2023-05-26 03:51:01.958664', 'CREATING', '8102c104-8280-40c3-8b4d-3628e687b3c4');
INSERT INTO public.invoice VALUES ('a662981a-a13e-4277-ad7a-c15d7830c1b3', '2023-05-29 16:19:53.444703', 'CREATING', '8102c104-8280-40c3-8b4d-3628e687b3c4');
INSERT INTO public.invoice VALUES ('9bf1476f-c961-4ec2-a35f-bbb38d2ef31d', '2023-05-29 16:20:35.702848', 'CREATING', '8102c104-8280-40c3-8b4d-3628e687b3c4');
INSERT INTO public.invoice VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', '2023-06-08 19:55:39.916108', 'CREATING', '9f61df38-9dbd-4ab1-95a1-b8789d44c9ce');
INSERT INTO public.invoice VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '2023-06-08 20:02:56.849218', 'CREATING', 'a5e01b25-3e8b-4bf6-be36-368fbc89f524');
INSERT INTO public.invoice VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '2023-06-08 20:08:38.208227', 'CREATING', 'bfe044f8-1f5d-40ec-bcfb-12d235daebc5');
INSERT INTO public.invoice VALUES ('c5f71697-f829-4e33-a06e-a1d7753064ca', '2023-06-08 20:11:13.404557', 'CREATING', '4ecaba59-748a-4948-905a-8d939c9c5d79');
INSERT INTO public.invoice VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '2023-06-08 20:15:04.981214', 'CREATING', 'f86a86ec-32f9-47df-9c48-1ee52ae1a531');
INSERT INTO public.invoice VALUES ('dd7195e8-8cb9-4a9a-9bde-7162fd49ddcb', '2023-06-08 20:16:12.697347', 'CREATING', 'f86a86ec-32f9-47df-9c48-1ee52ae1a531');
INSERT INTO public.invoice VALUES ('a54761bb-6799-47df-add8-53add78ad895', '2023-06-08 20:19:21.852894', 'CREATING', 'c5df6629-75d5-46f2-8152-417d420d1d6d');


--
-- TOC entry 3400 (class 0 OID 24876)
-- Dependencies: 217
-- Data for Name: ordered_book; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.ordered_book VALUES ('7e3def66-1ce1-4177-8877-1fc56448ac14', 'ELECTRONIC', 400, 1, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('56a99760-55cd-4f3a-99d5-63b4be4c1a67', 'ELECTRONIC', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('634ef351-3484-4ae7-8038-8f0af0c38467', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('28a46c4f-6278-424b-8357-8af0b08b6015', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('e5c01cc5-cd7d-4486-b7ed-9ade86c82fe1', 'PAPER', 400, 1, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('bbeaeb9f-1d50-4af2-9b4c-dc183e4d75ca', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('d9a71c8b-0695-4a8f-978c-8d493db4de9b', 'PAPER', 100, 2, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('890c0486-3932-455e-b165-3fd848e50d5e', 'PAPER', 400, 100, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('886226d5-ed28-4c6a-aae4-621a8f3028bd', 'PAPER', 1250, 1, 'isbn-3');
INSERT INTO public.ordered_book VALUES ('f0d9254e-6e91-46dc-8da3-75c233ca93a1', 'PAPER', 500, 1, 'isbn-4');
INSERT INTO public.ordered_book VALUES ('8850b561-3eab-4dc7-b610-bc67f8504b28', 'PAPER', 350, 1, '1617298697');
INSERT INTO public.ordered_book VALUES ('faf95338-4048-4af7-9671-42e78d116d62', 'PAPER', 400, 1, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('c8889ccc-817a-46b1-a88d-a7e9e1696646', 'PAPER', 193, 1, '0553211404');
INSERT INTO public.ordered_book VALUES ('512c870c-4ccd-41e4-af82-d491e1e7b6c4', 'PAPER', 400, 1, '0132350882');
INSERT INTO public.ordered_book VALUES ('068b4492-b2b3-47ec-941e-a8e88dfba4ce', 'PAPER', 89, 1, '9781409183082');
INSERT INTO public.ordered_book VALUES ('5afa03e9-d50b-4c86-93ec-26f13fa10857', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('b8120d33-0385-4a0b-ad09-bd1ce6445fdf', 'PAPER', 200, 1, '1668002175');
INSERT INTO public.ordered_book VALUES ('3a362d67-a52a-43e4-b74c-d5389771a909', 'PAPER', 250, 1, 'B07YN9YNP9');
INSERT INTO public.ordered_book VALUES ('fd8d064f-c53f-4743-8570-36716b616374', 'PAPER', 188, 1, '1501156705');
INSERT INTO public.ordered_book VALUES ('d784b8bf-6ce3-45d7-b6ad-944ceed4abb9', 'PAPER', 150, 1, '1982181117');
INSERT INTO public.ordered_book VALUES ('39b0226b-3743-4b89-a6db-cb8b2cbb44e4', 'PAPER', 100, 1, '0143127748');
INSERT INTO public.ordered_book VALUES ('2ededa4e-d57c-443d-b3c2-59aeeaf41599', 'PAPER', 255, 1, 'isbn-5');
INSERT INTO public.ordered_book VALUES ('f8ada020-9de0-4d07-a5db-920b8d413e0a', 'PAPER', 200, 1, '045149699X');
INSERT INTO public.ordered_book VALUES ('1ef3b7d4-fdec-44f8-99da-f0c0e71160af', 'PAPER', 250, 1, '1449334377');
INSERT INTO public.ordered_book VALUES ('0166dd67-fa75-4ccc-a07b-57178049fd6d', 'PAPER', 200, 1, 'B07N943MFX');
INSERT INTO public.ordered_book VALUES ('2e275998-7cda-4953-a9bb-a3508f7da9ac', 'PAPER', 500, 1, 'B00H6T77F4');
INSERT INTO public.ordered_book VALUES ('d32df787-8a7f-4b4a-be95-a309ad5a3375', 'PAPER', 150, 1, '9781476770390');
INSERT INTO public.ordered_book VALUES ('89f43cd1-83b8-4573-ac4f-69c569ce164a', 'PAPER', 350, 1, '978-0805094596');
INSERT INTO public.ordered_book VALUES ('da7ec999-70a0-40b5-81db-877cf132e005', 'PAPER', 350, 1, '978-1250127747');
INSERT INTO public.ordered_book VALUES ('5bca0fb4-71d7-4dfd-aca5-2104abb48d13', 'PAPER', 350, 1, '978-0805094602');
INSERT INTO public.ordered_book VALUES ('3a7724c6-9ab8-4bb6-9ad8-d14575efe5ce', 'PAPER', 250, 1, 'B077GXY6B7');
INSERT INTO public.ordered_book VALUES ('834254d8-c1a8-4b28-a787-2398d789b384', 'PAPER', 350, 1, '978-1627792127');
INSERT INTO public.ordered_book VALUES ('84898d2f-4783-494a-a1f9-dda7d0065fff', 'PAPER', 350, 1, '978-0805094619');
INSERT INTO public.ordered_book VALUES ('a4fad610-52bc-4341-a14b-0a21a8b51750', 'PAPER', 250, 1, 'B004R95LAO');
INSERT INTO public.ordered_book VALUES ('c742dc86-6990-48ee-9a9e-2f605f2bebeb', 'PAPER', 250, 1, 'B0049MPVFY');
INSERT INTO public.ordered_book VALUES ('85e92d3e-c6ce-4696-ad48-4f47fe3bebf4', 'PAPER', 250, 1, 'B000SEFM9C');
INSERT INTO public.ordered_book VALUES ('88655d64-1ecb-40f8-ae88-8da1729733e8', 'PAPER', 400, 1, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('14bfc566-8c1d-4344-9df9-a24ab3269039', 'PAPER', 1250, 1, 'isbn-3');
INSERT INTO public.ordered_book VALUES ('9eeeac88-ac0e-4e31-8555-e1dd12d60155', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('6cedf5ad-96a0-482b-9c15-1c06cf7eb3ad', 'PAPER', 250, 1, 'B077GXY6B7');
INSERT INTO public.ordered_book VALUES ('a8c61826-2a02-47b5-ae1a-f66291008e10', 'PAPER', 193, 1, '0553211404');
INSERT INTO public.ordered_book VALUES ('7eed771d-86b2-4d97-ac88-b9b413fcbca7', 'PAPER', 255, 1, 'isbn-5');
INSERT INTO public.ordered_book VALUES ('1576f5dd-ddc4-42c9-a865-af1488e16efb', 'PAPER', 89, 1, '9781409183082');
INSERT INTO public.ordered_book VALUES ('6f801ce0-e042-4f42-ba4c-cd5b3db581bc', 'PAPER', 500, 1, 'isbn-4');
INSERT INTO public.ordered_book VALUES ('48806485-d01f-45e1-b2ad-898439e1ceb2', 'PAPER', 200, 1, '1668002175');
INSERT INTO public.ordered_book VALUES ('17ef7c65-2441-4e16-aaa4-64baef69e3b8', 'PAPER', 500, 1, 'B00H6T77F4');
INSERT INTO public.ordered_book VALUES ('2750c6ea-20ab-4a11-9824-105307a07950', 'PAPER', 150, 1, '1982181117');
INSERT INTO public.ordered_book VALUES ('8160562b-9608-4a60-a2d2-5b6f8b78a9eb', 'PAPER', 200, 1, '045149699X');
INSERT INTO public.ordered_book VALUES ('60523468-9835-4fbf-b31a-53f2893fe7d6', 'PAPER', 400, 1, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('551aa5f4-c0ed-46f9-9e7b-a9088a7b4e45', 'PAPER', 1250, 1, 'isbn-3');
INSERT INTO public.ordered_book VALUES ('5ca4c69d-cb46-422e-aa9b-0f1623c330c0', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('c9a54e4f-00ae-4722-98ea-9a3ee3461f35', 'PAPER', 100, 1, 'isbn-2');
INSERT INTO public.ordered_book VALUES ('7ba20a2a-f629-4d8c-9c1b-fd43d0e9231c', 'PAPER', 400, 1, 'isbn-1');
INSERT INTO public.ordered_book VALUES ('acf4c6e8-3295-4a34-b4a4-6848e2b24565', 'PAPER', 1250, 1, 'isbn-3');


--
-- TOC entry 3399 (class 0 OID 24869)
-- Dependencies: 216
-- Data for Name: invoice_books_in_order; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.invoice_books_in_order VALUES ('2e94da1f-8155-4880-bdb7-04b4f15aa8ea', '7e3def66-1ce1-4177-8877-1fc56448ac14');
INSERT INTO public.invoice_books_in_order VALUES ('f3426e2e-bd68-477c-b6ad-483ca7c49bb3', '56a99760-55cd-4f3a-99d5-63b4be4c1a67');
INSERT INTO public.invoice_books_in_order VALUES ('f9f6a753-e042-4572-a4ea-8c09a228aefd', '634ef351-3484-4ae7-8038-8f0af0c38467');
INSERT INTO public.invoice_books_in_order VALUES ('eb7b5629-9add-4c42-b43b-007b3e0392d4', '28a46c4f-6278-424b-8357-8af0b08b6015');
INSERT INTO public.invoice_books_in_order VALUES ('b8d2055d-33ad-43e4-a0a9-431268393fdc', 'e5c01cc5-cd7d-4486-b7ed-9ade86c82fe1');
INSERT INTO public.invoice_books_in_order VALUES ('76d7edb6-d411-46f3-88e9-f7c1e3f8a75d', 'bbeaeb9f-1d50-4af2-9b4c-dc183e4d75ca');
INSERT INTO public.invoice_books_in_order VALUES ('a662981a-a13e-4277-ad7a-c15d7830c1b3', 'd9a71c8b-0695-4a8f-978c-8d493db4de9b');
INSERT INTO public.invoice_books_in_order VALUES ('9bf1476f-c961-4ec2-a35f-bbb38d2ef31d', '890c0486-3932-455e-b165-3fd848e50d5e');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', '886226d5-ed28-4c6a-aae4-621a8f3028bd');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', 'f0d9254e-6e91-46dc-8da3-75c233ca93a1');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', '8850b561-3eab-4dc7-b610-bc67f8504b28');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', 'faf95338-4048-4af7-9671-42e78d116d62');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', 'c8889ccc-817a-46b1-a88d-a7e9e1696646');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', '512c870c-4ccd-41e4-af82-d491e1e7b6c4');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', '068b4492-b2b3-47ec-941e-a8e88dfba4ce');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', '5afa03e9-d50b-4c86-93ec-26f13fa10857');
INSERT INTO public.invoice_books_in_order VALUES ('0b5ca631-b5de-4db3-b26d-771a2043aa94', 'b8120d33-0385-4a0b-ad09-bd1ce6445fdf');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '3a362d67-a52a-43e4-b74c-d5389771a909');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', 'fd8d064f-c53f-4743-8570-36716b616374');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', 'd784b8bf-6ce3-45d7-b6ad-944ceed4abb9');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '39b0226b-3743-4b89-a6db-cb8b2cbb44e4');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '2ededa4e-d57c-443d-b3c2-59aeeaf41599');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', 'f8ada020-9de0-4d07-a5db-920b8d413e0a');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '1ef3b7d4-fdec-44f8-99da-f0c0e71160af');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '0166dd67-fa75-4ccc-a07b-57178049fd6d');
INSERT INTO public.invoice_books_in_order VALUES ('90192fba-5207-4a90-977e-8ab9692e717b', '2e275998-7cda-4953-a9bb-a3508f7da9ac');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', 'd32df787-8a7f-4b4a-be95-a309ad5a3375');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '89f43cd1-83b8-4573-ac4f-69c569ce164a');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', 'da7ec999-70a0-40b5-81db-877cf132e005');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '5bca0fb4-71d7-4dfd-aca5-2104abb48d13');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '3a7724c6-9ab8-4bb6-9ad8-d14575efe5ce');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '834254d8-c1a8-4b28-a787-2398d789b384');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '84898d2f-4783-494a-a1f9-dda7d0065fff');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', 'a4fad610-52bc-4341-a14b-0a21a8b51750');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', 'c742dc86-6990-48ee-9a9e-2f605f2bebeb');
INSERT INTO public.invoice_books_in_order VALUES ('a4439012-427e-4d8e-83ec-fae9cb57cdc0', '85e92d3e-c6ce-4696-ad48-4f47fe3bebf4');
INSERT INTO public.invoice_books_in_order VALUES ('c5f71697-f829-4e33-a06e-a1d7753064ca', '88655d64-1ecb-40f8-ae88-8da1729733e8');
INSERT INTO public.invoice_books_in_order VALUES ('c5f71697-f829-4e33-a06e-a1d7753064ca', '14bfc566-8c1d-4344-9df9-a24ab3269039');
INSERT INTO public.invoice_books_in_order VALUES ('c5f71697-f829-4e33-a06e-a1d7753064ca', '9eeeac88-ac0e-4e31-8555-e1dd12d60155');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '6cedf5ad-96a0-482b-9c15-1c06cf7eb3ad');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', 'a8c61826-2a02-47b5-ae1a-f66291008e10');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '7eed771d-86b2-4d97-ac88-b9b413fcbca7');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '1576f5dd-ddc4-42c9-a865-af1488e16efb');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '6f801ce0-e042-4f42-ba4c-cd5b3db581bc');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '48806485-d01f-45e1-b2ad-898439e1ceb2');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '17ef7c65-2441-4e16-aaa4-64baef69e3b8');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '2750c6ea-20ab-4a11-9824-105307a07950');
INSERT INTO public.invoice_books_in_order VALUES ('2debe12d-cb0a-4261-afee-d91c00aa3057', '8160562b-9608-4a60-a2d2-5b6f8b78a9eb');
INSERT INTO public.invoice_books_in_order VALUES ('dd7195e8-8cb9-4a9a-9bde-7162fd49ddcb', '60523468-9835-4fbf-b31a-53f2893fe7d6');
INSERT INTO public.invoice_books_in_order VALUES ('dd7195e8-8cb9-4a9a-9bde-7162fd49ddcb', '551aa5f4-c0ed-46f9-9e7b-a9088a7b4e45');
INSERT INTO public.invoice_books_in_order VALUES ('dd7195e8-8cb9-4a9a-9bde-7162fd49ddcb', '5ca4c69d-cb46-422e-aa9b-0f1623c330c0');
INSERT INTO public.invoice_books_in_order VALUES ('a54761bb-6799-47df-add8-53add78ad895', 'c9a54e4f-00ae-4722-98ea-9a3ee3461f35');
INSERT INTO public.invoice_books_in_order VALUES ('a54761bb-6799-47df-add8-53add78ad895', '7ba20a2a-f629-4d8c-9c1b-fd43d0e9231c');
INSERT INTO public.invoice_books_in_order VALUES ('a54761bb-6799-47df-add8-53add78ad895', 'acf4c6e8-3295-4a34-b4a4-6848e2b24565');
