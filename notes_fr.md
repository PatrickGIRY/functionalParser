# Analyseur syntaxique fonctionnel

## Qu'est qu'un analyseur syntaxique ?

Un analyseur syntaxique analyse une chaîne de caractères pour produire un arbre syntaxique.

## Définissons un analyseur syntaxique par une fonction

Nous pouvons définir un analyseur syntaxique comme une fonction qui prend en entrée une chaîne de caractères et qui produit en résultat un arbre.

> En java nous pouvons définir une fonction à l'aide d'une interface

```java
@FunctionalInterface
public interface Parser {
    Tree parse(String input);
}
```

Cette définition est correcte, mais pas pratique.
Elle n'est pas composable.
Nous voudrions décomposer cet analyseur syntaxique en analyseurs syntaxiques plus simples.
Ces analyseurs syntaxiques ne consommeront pas toute la chaîne de caractères en entrée.
Si l'analyse du début de la chaîne de caractères est un succés, alors le résultat obtenu est un arbre et la chaîne restant à analyser. On peut définir celà comme ceci :

```java
@FunctionalInterface
public interface Parser {
    Success parse(String input);

    record Success(Tree tree, String remainingInput) {}
}
```
