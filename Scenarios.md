# Scenarios

```gherkin
Given material <material> with in stock <currentlyInStock>
  And reserve material for ticket 1 is <reserve>
  Then in stock is [inStock]
  And reserved for ticket is [reserved]

| material | currentlyInStock | reserve | inStock | reserved |
| Schroefje | 10 | 7 | 10 | 7 |
```
