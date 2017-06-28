# Order: 4
# Milestone: R3

Feature: Filter by Ingredients
  In order to find pizzas quickly
  I can filter them by ingredients

  Scenario Outline: Filter by category
    Given our default pizzas
    When I filter the list of Pizzas for Ingredients <ingredients>
    Then I see <pizzas>
    But I dont see <filtered-pizzas>

    Examples:
      | ingredients            | pizzas | filteredPizzas
      | Pilze                  | Funghi, Prosciutto & Funghi, Quattro Stagioni | Margarita, Prosciutto, Diavolo
      | Pilze, Schinken        | Prosciutto & Funghi | Margarita, Funghi, Prosciutto
      | Pepperoncini           | Diavolo, Vulcano    | Margarita, Funghi, Prosciutto, Hawai
