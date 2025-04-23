Gradebook by Ben, Behruz, Davran and Fatih
Before running, note that certain external libraries are required. (These might come bundled with the runnable)
To run, first boot up the jar (of course), then you will be prompted for how you want to load it.
If you want to go by txt, make sure you have a students.txt, teachers.txt, courses.txt, and in that courses.txt, have the file names of the other courses you want to add.
Each (coursename).txt will have the course description at the top and a list of the students inside the course below.
Alternatively, you can load by json, which simply handles everything when properly made.
Next, you need to log in. You need to log in as a student or teacher, thus you must provide their username. You will notice it tells you to create a password - first logins will always have you create the password instead of entering one.
Once you are in, you can edit / view / etc as described in the text. Students only need to concern themselves, thus their instructions are only concerned with themselves, and they do not need to open a specific course.
Teachers DO need to go by course, so when you log in as a teacher, you must open a course. The course grading style will be set if it wasn't already (loading from txt will always have it unset), and then they can edit the course as expected.
Press 0 in any case to log out.
You'll notice that now, you DO need the passwords to log back in. These passwords are encrypted with Argon 2 along with a securerandom function, with Argon2 being a secure password hashing algorithm that won the Password Hashing Competition in 2015. It’s designed to be memory-hard, making it resistant to brute-force attacks. Argon2 hashes a password by first mixing the password and salt into a pseudo-random state, then filling a large memory array with data-dependent computations. And SecureRandom able to taking the password and make a near unguessable salt to be attached onto it - but luckily, still repeatable.
Once you are done with the program, you probably want to save it. Have a .json ready, give it the json name, and it will save the *entire* codebase to it. You can load it later to start up right where you left off - there will be no changes, apart from maybe you not having the print history.

Now then, as for the code design.
The code was split into many classes, but the ones which matter:
Database (Contains Courses, Users, and encrypted passwords. It also contains other data which is reconstructed on load. Fun fact: The 3 listed pieces of data here either are or contain the only things which are saved in json. This method means there is NO DUPLICATES, more on that in a second.)
Account Storage (Contains Users, encrypted passwords, and models).
Models (Courses, Students, Teachers).
Students & Teachers both know which courses they're in.
Courses (Students enrolled, Teacher, and Assignments)
Assignments (No notable references)
Note these are not including the abstract or one-off classes.

There is also the Main and View. The Main is what's ran and is what has the user log in. The view is simply how you navigate the program after you've logged in. However, we have made sure to minimize escaping references & overall limit what the front end can do as much as possible.

Finally, AI. We did not use any AI in this entire project. Some AI proposals were made, but none ever actually made it to the final product. Everything here was typed by us!
Have fun! Try not to edit the json too much!
