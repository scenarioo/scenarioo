# Order: 1
# Milestone: R2

Feature: See Pizza Details

  Scenario Outline: See Pizza Details
    Given our standard pizza catalogue
    And I am in Switzerland
    When I browse to Pizza <pizza>
    Then I can see the <ingredients>
    And I can see the picture <picture>
    And I can see the standard price for this pizza is <price>
    And I can see description <description>

    Examples:
      | pizza       | ingredients                  | picture        | description |
      | Margarita   | Tomaten, Mozzarella, Oregano | margarita.png  | so wurde die Pizza gemacht, als sie erfunden wurde |
      | Funghi      | Tomaten, Mozzarella, Champignons, Oregano  | funghi.png | garantiert kein Schimmel, nur frische Champignons! |

