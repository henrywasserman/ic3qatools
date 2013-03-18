echo off
echo.
echo ###################################################################################################
echo # This script lists all tests by ID, Description, and the containing REQ file.                    #
echo # If you wish to filter the list, pass in a string as a parameter: (e.g.  list-tests.bat dealmap) #
echo # This will do a case-insensitive grep on the output.                                             #
echo ###################################################################################################
echo.
echo ID	Test Description	Test REQ File

set varname=filter
set filter=%1
if [%1]==[] (
 set filter=""
)


\cygwin\bin\grep TESTCASE ../data/infogroup/*.req | \cygwin\bin\sed -n 's/\(.*\.req\).*TESTCASE\s*\([0-9a-zA-Z]*\),\s*\(.*\)/\2\t\3\t\1/p' | \cygwin\bin\sort | \cygwin\bin\grep -i %filter%
