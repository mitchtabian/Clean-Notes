# I got these colors from a medium article. Can't remember the author.
# Functions for customizing colors(Optional)
print_red(){
    printf "\e[1;31m$1\e[0m"
}
print_green(){
    printf "\e[1;32m$1\e[0m"
}
print_yellow(){
    printf "\e[1;33m$1\e[0m"
}
print_blue(){
    printf "\e[1;34m$1\e[0m"
}

#Start
print_blue "\n\n\nStarting"

print_blue "\n\n\ncd into working directory...\n"
cd "/d/Android Studio Projects/CleanNotes/"

print_blue "\n\n\nrun unit tests...\n"
./gradlew test
print_green "\n\n\n unit tests COMPLETE.\n"

print_blue "\n\n\n run androidTests...\n"
./gradlew connectedAndroidTest
print_green "\n\n\n androidTests COMPLETE.\n"

print_green "\n\n\n Done.\n"