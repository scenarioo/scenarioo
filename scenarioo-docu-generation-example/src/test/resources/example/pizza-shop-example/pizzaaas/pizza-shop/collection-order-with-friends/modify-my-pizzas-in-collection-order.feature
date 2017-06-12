# Order: 2
# Milestone: R1

Feature: Add Pizza To Collection Order

  Scenario: Add Pizza to Order anonymous
    Given I received a link for a collection order
    And I am not logedin as registered user
    When I browse to that link
    And I select a Pizza and enter my name and email
    Then that Pizza gets added to that order under my name.
    And I receive a confirmation email with a link, where I could later change my selection.
