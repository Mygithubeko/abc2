
@SPCreation
Feature: Creation of a SP account


  Scenario: Verify that SP should be able to navigate to AuthO registration page
    Given SPorUser is on homepage
    When SP clicks Register Your Service link on Homepage
    Then SP should navigate to Wentsy Introduction Page
    When SP clicks Register button on Wentsy Introduction Page
    Then SP navigates to SP Auth0 registration page

  @SPSignup
  Scenario: Verify that SP should be able to sign up a new SP account

    Given SP is on SP Auth0 registration page
    When SP clicks Register button on SPAuthOPage
    And SP enters valid email address and password
    Then  SP should see registration message on SP Panel
    And SP should navigate to SP Panel

  @SPnotSignUp
  Scenario: Verify that SP should not be able to sign up a new SP account with invalid credentials
    Given SP is on SP Auth0 registration page
    When SP clicks Register button on SPAuthOPage
    And user enters invalid credentials
    Then user should not navigate to SP Panel with wrong credentials

  @SPActivateAsCompany
  Scenario: Verify that new SP should be able to complete registration as a company
    Given SP is on Panel homepage
    When SP clicks Register button on SPAuthOPage
    And SP enters valid email address and password
    And SP clicks Activate button on Panel homepage
    And SP fills out all required information the registration form as a company
    And SP clicks activate button under confirmation message
    And admin approves new SP registration
    Then registration status should be seen as Approved under My account info

  @SPActivateAsIndividual
  Scenario: Verify that new SP should be able to complete registration as an individual
    Given SP is on Panel homepage
    When SP clicks Register button on SPAuthOPage
    And SP enters valid email address and password
    And SP clicks Activate button on Panel homepage
    And SP fills out all required information the registration form as an individual
    And SP clicks activate button under confirmation message
    And admin approves new SP registration
    Then registration status should be seen as Approved under My account info

  @SPnotRegister
  Scenario: Verify that new SP should not be able to complete registration with invalid information
    Given SP is on Panel homepage
    When SP clicks Register button on SPAuthOPage
    And SP enters valid email address and password
    And SP clicks Activate button on Panel homepage
    And SP fills out the registration form with invalid information
    Then SP should not be able to complete registration



