# Functional error handling

Some notes on how to handle errors in a functional way.

This is HEAVILY based on [this presentation](https://speakerdeck.com/raulraja/functional-error-handling) and 
[this blog entry](https://blog.rockthejvm.com/idiomatic-error-handling-in-scala/)

Some other useful links:
- https://medium.com/@lettier/your-easy-guide-to-monads-applicatives-functors-862048d61610
- https://softwaremill.com/applicative-functor/
- https://adit.io/posts/2013-04-17-functors,_applicatives,_and_monads_in_pictures.html
- https://www.innoq.com/en/blog/golang-errors-monads/  (this is interesting: rediscovering Monads in Go)

## Disclaimer
Some bits of the code are copy/pasted and could be implemented in a better way. They are not the main focus of this
session though