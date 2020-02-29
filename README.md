# Sheriff of Nottingham - Cosmin-Razvan VANCEA - 323CA

## Pachete

- Tema este impartita in 4 pachete, fiecare pachet fiind raspunzator pentru o
  anumita parte a logicii jocului:
  - `common`: contine constante si diverse functii de comparare.
  - `goods`: clasele necesare lucrului cu bunurile si toate detaliile asociate
  acestora (tip, profit, penalty etc)
  - `main`: raspunzator pentru incarcarea datelor, crearea diferitelor
  structuri de date necesare si pentru structura generala a jocului
  (impartirea pe runde, subrunde si categorii de jucatori)
  - `players`: contine implementarile strategiilor ce pot fi abordate de jucatori

## Logica

- Clasa `Main` este punctul de intrare in program. Aici se incarca datele, se
  populeaza clasa raspunzatoare tinerii evidentei cartilor (`GameCards`), se
  creeaza jucatorii.
- Jucatorii implementeaza toti interfata `IPlayer` care descrie toate actiunile
  ce pot fi intreprinse de un jucator sau asupra unui jucator.
- Tipurile de jucator sunt: basic, bribed, greedy. Bribed si greedy mostenesc
  jucatorul basic.
- Clasa `Inventory<T>` este o implementare de inventar care pentru fiecare bun
  memoreaza si de cate ori apare acesta. Esential, este un `Map`, dar este
  implementat peste un `LinkedList`, deci spre deosebire `Map`, inventarul este
  sortabil.
- Clasa InspectionBag extinde clasa Inventory si permite specificarea unei mite
- Un jucator inglobeaza 3 clase de tip `Inventory<Goods>`:
  - `inventory`: contine ultimele 10 carti trase de jucator din care va alege 8
  - `inspectionBag`: cele (max) 8 carti pe care jucatorul le va da spre inspectie
  impreuna cu suma de bani reprezentand mita
  - `savedInventory`: aici sunt salvate cartile ce trec de inspectie
- Cum toti jucatorii sunt fie `Basic`, fie extind `Basic`, atunci majoritatea
  logicii lor este implementata in `Basic`, iar celelalte 2 clase doar suprascriu
  metodele de inspectie si de creare a sacului dat spre inspectie.
  
## Probleme intalnite

- Cred ca testele nu respecta intocmai strategia bribed:
  - cand se joaca strategia bribed, jucatorul va declara ca are in sac mere
  - deci, asta inseamna ca jucatorul ar putea sa adauge in sac oricate mere
    poate deoarece acestea nu vor fi penalizate de sheriff
  - insa sunt cateva teste care pica daca pun aceasta conditie
