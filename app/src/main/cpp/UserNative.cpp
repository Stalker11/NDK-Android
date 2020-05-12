//
// Created by bakiser on 5/12/20.
//

#include "UserNative.h"
UserNative::UserNative()
    : m_age(-1)
{
    //m_firstName; by default string is empty
    //m_lastName; by default string is empty
}

void UserNative::setFirstName(const string& name)
{
    m_firstName = name;
}

void UserNative::setSecondName(const string& name)
{
    m_secondName = name;
}

void UserNative::setage(const int age)
{
    m_age = age;
}

string UserNative::getFirstName()
{
    return m_firstName;
}

string UserNative::getSecondName()
{
    return m_secondName;
}

int UserNative::getAge()
{
    return m_age;
}

UserNative& UserNative::getIntance()
{
    static UserNative un;
    return un;
}