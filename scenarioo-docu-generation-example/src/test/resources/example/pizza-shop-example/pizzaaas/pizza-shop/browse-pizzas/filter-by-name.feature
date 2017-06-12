# Order: 2
# Milestone: R1

Feature: Filter by Name
  In order to find pizzas quickly
  I can filter them by part of their name

  Scenario Outline: Filter by part of name
    Given our default pizzas
    When I filter the list of Pizzas for text <filterText>
    Then I see <pizzas>
    But I dont see <filteredPizzas>

    Examples:
      | filterText           | pizzas | filteredPizzas |
      | Fun                  | Funghi | Margarita, Prosciutto, Diavolo |
      | di                   | Diavolo, di Paolo, di Franco | Margarita, Funghi, Prosciutto |
      | Paolo                | di Paolo, Paolo's Speciale   | Margarita, Funghi, Prosciutto |

