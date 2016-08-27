**** Settings ***
Library  DatabaseLibrary

***** Test Cases ***
Use Keywords of an external Java Keyword Library
  Run Keyword And Expect Error  No connection open.*  Table Must Exist  DEMOTABLE
