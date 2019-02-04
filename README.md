# Auxiliary clojure library for common functions

This is a collection of functions mostly borrowed from other libraries
as
[suchwow](https://github.com/marick/suchwow/),
[clojure-commons](https://github.com/marick/clojure-commons)
and [hara](https://github.com/zcaudate/hara) to complete the namespace
of clojure core functions with intuitively named and easy to use
functions.

One particular requirement for this "auxiliary" collection is to avoid
adding further dependencies, hence the extraction of simple functions
from the aforementioned frameworks: here we take base functions
avoiding the proliferation of further dependencies.

<a href="https://www.dyne.org"><img
src="https://secrets.dyne.org/static/img/swbydyne.png"
alt="software by Dyne.org"
titlxe="software by Dyne.org" class="pull-right"></a>

[Getting started](#Getting-Started) | [Prerequisites](#Prerequisites) | [Running](#Running) | [Running the tests](#Running-the-tests) | [Deployment](#Deployment) | [Todos](#Todos) | [Acknowledgements](#Acknowledgements) | [Licence](#Licence) | [change log](https://github.com/Dyne/clj-auxiliary/blob/master/CHANGELOG.md) 

[![Clojars Project](https://clojars.org/org.clojars.dyne/freecoin-lib/latest-version.svg)](https://clojars.org/org.clojars.dyne/auxiliary)
[![License: AGPL v3](https://img.shields.io/badge/License-AGPL%20v3-blue.svg)](https://www.gnu.org/licenses/agpl-3.0)

## Getting started

Insert into your project `:dependencies`:

```
[org.clojars.dyne/auxiliary "0.5.0"]

```

## Acknowledgements

Code taken from projects like: 
- https://github.com/marick/suchwow/
- https://github.com/marick/clojure-commons
- https://github.com/zcaudate/hara


## Licence

This project is licensed under the AGPL 3 License - see the [LICENCE](LICENCE) file for details

#### Additional permission under GNU AGPL version 3 section 7.

If you modify Freecoin-lib, or any covered work, by linking or combining it with any library (or a modified version of that library), containing parts covered by the terms of EPL v 1.0, the licensors of this Program grant you additional permission to convey the resulting work. Your modified version must prominently offer all users interacting with it remotely through a computer network (if your version supports such interaction) an opportunity to receive the Corresponding Source of your version by providing access to the Corresponding Source from a network server at no charge, through some standard or customary means of facilitating copying of software. Corresponding Source for a non-source form of such a combination shall include the source code for the parts of the libraries (dependencies) covered by the terms of EPL v 1.0 used as well as that of the covered work.
