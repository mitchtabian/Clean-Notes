print_blue(){
    printf "\e[1;34m$1\e[0m"
}


print_blue "\n\n\nStarting Firestore Local Emulator...\n"
firebase emulators:exec --only functions,firestore "ui_and_unit_tests.sh"














