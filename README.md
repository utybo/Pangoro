# Heads up!

**Pangoro and Lixy have been replaced by [Tegal Niwen](https://tegral.zoroark.guru/docs/modules/core/niwen) ([GitHub](https://github.com/utybo/Tegral)).** This evolved version of Pangoro and Lixy feature parser type-safety, more expectations and matchers, an awesome execution debugger and much more! This repository will no longer be updated.

## Previous README

# [![Pangoro](https://zoroark.guru/img/pangoro.png)](http://pokemondb.net/pokedex/pangoro) Pangoro, the parser with a nice Kotlin DSL

[![Actions Status JVM](https://img.shields.io/github/workflow/status/utybo/Pangoro/JVM%20tests?style=for-the-badge&logo=github&label=tests%20(JVM))](https://github.com/utybo/Pangoro/actions)
[![Actions Status JS](https://img.shields.io/github/workflow/status/utybo/Pangoro/JS%20tests?style=for-the-badge&logo=github&label=tests%20(JS))](https://github.com/utybo/Pangoro/actions)
[![Code Climate coverage](https://img.shields.io/codeclimate/coverage/utybo/Pangoro?style=for-the-badge&logo=Code-Climate)](https://codeclimate.com/github/utybo/Pangoro/test_coverage)
[![Code Climate maintainability](https://img.shields.io/codeclimate/maintainability/utybo/Pangoro?style=for-the-badge&logo=Code-Climate)](https://codeclimate.com/github/utybo/Pangoro/maintainability)
![Made with Kotlin](https://img.shields.io/badge/Made%20with-Kotlin-blue?logo=Kotlin&style=for-the-badge)

![Experimental](https://img.shields.io/badge/Stage-Experimental-red?style=flat-square) [![Release](https://jitpack.io/v/guru.zoroark/pangoro.svg?style=flat-square)](https://jitpack.io/#guru.zoroark/pangoro) Pangoro is under active development.

[**>>> Documentation <<<**](https://docs.zoroark.guru/#/pangoro/)

Pangoro is a parser intended for use with the [Lixy](https://github.com/utybo/Lixy) lexer, although it can also work with anything, provided that you convert your tokens into Lixy tokens.

```kotlin
// Types used by the parser (PangoroNode) and lexer (LixyTokenType)
data class Number(val value: String) : PangoroNode {
    companion object : PangoroNodeDeclaration<Number> by reflective()
}

data class Sum(val first: Number, val second: Number) : PangoroNode {
    companion object : PangoroNodeDeclaration<Addition> by reflective()
}

enum class Tokens : LinkTokenType {
    Number, Plus
}

// Lexer (from Lixy)
val lexer = lixy {
    state {
        matches("\\d+") isToken Tokens.Number
        "+" isToken Tokens.Plus
        " ".ignore
    }
}    
               
// Parser (from Pangoro)
val parser = pangoro {
    Number {
        expect(Tokens.Number) storeIn "value"
    }
    Sum root {
        expect(Number) storeIn "first"
        expect(Tokens.Plus)
        expect(Number) storeIn "second"
    }
}
val tokens = lexer.tokenize("123 + 4567")
val ast: Sum = parser.parse(tokens)
/* 
 * ast = Sum(
 *     first = Number(value = "123"),
       second = Number(value = "4567")
 * )
 */
```

## Getting Pangoro

You can get the following artifacts from Jitpack:

* Kotlin/JVM: `guru.zoroark.pangoro:pangoro-jvm:version`
* Kotlin/JS: `guru.zoroark.pangoro:pangoro-js:version`
* Kotlin MPP: `guru.zoroark.pangoro:pangoro:version`

[![Zoroark](https://img.pokemondb.net/sprites/black-white/anim/normal/zoroark.gif)](https://zoroark.guru)
