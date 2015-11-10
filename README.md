#COPADS

Project 1:
Given a database file of users and their password hashes(Each password is hashed by apply SHA-256 to the password 100,000 
times) and a dictionary of potential passwords, compute each potential password in the dictionary and compare the calcualted
hash to the database file of password hashes. 

Java program uses multiple threads to carry out the dictionary attack.

Project 2:
Given database file of users and their password hashes, find passwords of length 1 to 4 characters, where each character is a lowercase letter from a to z and digits 0 to 9. Go thru every password, compute its hash and check if it matches any user
in the database. 

Java program uses Parallel Java 2 Library to carry out brute force attack

## History

Project CSCI-251 Concepts of Parallel Distributed Systems

## Credits

Author: Robin Li

## License

The MIT License (MIT)

Copyright (c) 2015 Robin Li

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
