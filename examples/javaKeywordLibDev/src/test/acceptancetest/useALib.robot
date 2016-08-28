**** Settings ***
Library  TestLibrary

***** Test Cases ***
Use Keywords of an external Java Keyword Library
  ${result}=  test
  Should Be Equal  ${result}  test keyword called ...

