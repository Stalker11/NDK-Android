//
// Created by bakiser on 5/12/20.
//

#ifndef NDK_ANDROID_USERNATIVE_H
#define NDK_ANDROID_USERNATIVE_H

#include <string>

class UserNative {
    string m_firstName;
    string m_lastName;
    int    m_age;

    public:
        UserNative();

        void   setFirstName(const string& name);
        void   setSecondName(const string& name);
        void   setage(const int age);
        string getFirstName();
        string getSecondName();
        int    getAge();

        static UserNative& getIntance();
};



#endif //NDK_ANDROID_USERNATIVE_H
