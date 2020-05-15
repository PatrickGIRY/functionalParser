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

Mais l'analyse peut échouer également. Du coup le résultat de l'analyse est optionnel :

```java
@FunctionalInterface
public interface Parser {
    Optional<Success> parse(String input);

    record Success(Tree tree, String remainingInput) {}
}
```

L'analyseur syntaxique ne produit pas forcément un arbre, mais une valeur. Nous devons généraliser la définition :

```java
@FunctionalInterface
public interface Parser<T> {
    Optional<Success<T>> parse(String input);

    record Success<T>(T matchedObject, String remainingInput) {}
}
```

## Analyseur syntaxique de base

### Analyseur syntaxique `item`

Ce premier analyseur syntaxique `item` échoue si l'entrée est vide, et consomme le premier caractère de l'entrée sinon.

```java
@Test
@DisplayName("fails if the input is empty")
public void fails_if_the_input_is_empty() {
  var parser = ParserChar.item();

  var result = parser.parse("");

  assertThat(result).isEmpty();
}

@Test
@DisplayName("consumes the first character of input if it is not empty")
public void consumes_the_first_character_of_the_input_if_it_is_not_empty() {
  var parser = ParserChar.item();

  var result = parser.parse("A");

  assertThat(result).isPresent();
            assertThat(result.map(Success::matchedChar)).hasValue('A');
            assertThat(result.map(Success::remainingInput)).hasValue("");
}
```

> En java les caractères sont définis avec un type primitif `char`.
> Les types primitfs ne peuvent pas être définit comme générique. C'est pour cette raison que nous définissons les types `ParserChar` et `Success` qui contient une valeur de type `char`.

```java
@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input.isEmpty()
                ? Optional.empty()
                : Optional.of(new Success(input.charAt(0), input.substring(1)));
    }

    Optional<Success> parse(String input);

    record Success(char matchedChar, String remainingInput) {}
}
```

> En java l'entrée peut valoir `null`.

```java
@Test
@DisplayName("fails if the input is null")
public void fails_if_the_input_is_null() {
  var parser = ParserChar.item();

  var result = parser.parse(null);

  assertThat(result).isEmpty();
}

@FunctionalInterface
public interface ParserChar {
    static ParserChar item() {
        return input -> input == null || input.isEmpty()
                ? Optional.empty()
                : Optional.of(new Success(input.charAt(0), input.substring(1)));
    }

    Optional<Success> parse(String input);

    record Success(char matchedChar, String remainingInput) {}
}
```

### Analyseur syntaxique `failure`

Cet analyseur échou tout le temps.

```java
@Test
@DisplayName("always fails")
public void always_fails() {
  var parser = ParserChar.failure();

  var result = parser.parse("Any input");

  assertThat(result).isEmpty();
}

@FunctionalInterface
public interface ParserChar {
  ...
  static ParserChar failure() {
    return input -> Optional.empty();
  }
  ...
}
```

### Analyseur syntaxique `valueOf`

Cet analyseur est en succés tout le temps. Le caractère fournit et la chaîne de caractères à analyser sont dans le résultat de l'analyse.

```java
@Test
@DisplayName("always success")
public void always_success() {
  var parser = ParserChar.valueOf('A');

  var result = parser.parse("Any input");

  assertThat(result).isPresent();
    assertThat(result.map(Success::matchedChar)).hasValue('A');
    assertThat(result.map(Success::remainingInput)).hasValue("Any input");
}

@FunctionalInterface
public interface ParserChar {
  ...
  static ParserChar valueOf(char c) {
    return input -> Optional.of(new ParserChar.Success('A', "Any input"));
  }
  ...
}
```

## Cominer les analyseurs syntaxiques en séquence

### Transformer le caractère en résultat de l'analyse

Supposons que nous voulions crée un analyser qui transforme le caractère en majuscule du résultat en un caractère minuscule.

```java
@Test
@DisplayName("transform a character into an other character")
public void transform_a_character_into_an_other_character() {
  var parser = ParserChar.valueOf('A').map(Character::toLowerCase);

  var result = parser.parse("Any input");

  assertThat(result).isPresent();
  assertThat(result.map(Success::matchedChar)).hasValue('a');
  assertThat(result.map(Success::remainingInput)).hasValue("Any input");
}
```
