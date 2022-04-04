# Movie/Actor-Database
Action:
Creez clasa abstracta Action si imi initializez variabilele cu care urmeaza sa
lucrez.

Command:
Am creat un constructor cu care imi iau parametrii(valabil pt toate clasele create).
In metoda execute apelez un if care verifica ce tip de comanda este folosita:
favorite, view sau rating si in functie de comanda receptionata verifica in ce
conditie se afla titlul fata de dorinta userului (daca este deja lafavorite sau nu,
de cate ori a fost vazut etc.) si executa comanda respectiva.

Query:
In metoda execute verificam ce input avem. Daca avem I actors: si parametrul 1)average
le gasim ratingul si filmografia de la filme si seriale. Daca avem 2)awards verificam
daca actorul a primit cel putin 1 din fiecare premiu din test si dupa salvam numarul lor si dupa sortam actorii (in functie de criteriul de sortare) in functie de numarul premiilor.
3)filter_description in functie de cheia pe care o are fiecare actor sortam in ordine crescatoare.II movies & shows le verificam ratingurile, daca sunt la favorites, dupa lungime si dupa numarul de vizualizari, iar in cazul III users ii ordonam dupa numarul de ratinguri acordate.La final sortam in functie rezultatele obtinute din ce cazuri am avut si dupa afisam.

Recommendation:
Avem 2 cazuri, atunci cand utilizatorul este premium sau este utilizator basic.
Un utilizator basic are permisiuni doar pentru recomandarile de tipul standard si
best_unseen, iar cei premium mai au si recomandari de tipul popular, favorite si search.
La standard verificam daca e film sau serial sau daca putem sa oferim o recomandare sau nu
si intoarce primul film nevazut din baza de date.
best_unseen - cel mai bun video nevizualizat de acel user.
popular - primul video din cel mai popular gen de film/serial
favorite - cel mai des intalnit video din listele de favorite ale tuturor utilizatorilor
search - toate filmele nevazute dintr-un anumit gen.
In cazul in care nu se poate recomanda un titlu (nu este utilizator premium, nu avem ce
sa recomandam etc.) intoarcem un mesaj care afiseaza acest lucru.

Pair:
Am 4 comparatori pentru a putea afisa in ordine actorii dupa nume si rating.
Pentru tot ce tine de sortare folosesc clasa comparator Pair. 

