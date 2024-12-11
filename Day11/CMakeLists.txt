cmake_minimum_required(VERSION 3.30)
project(Day11)

set(CMAKE_CXX_STANDARD 23)

configure_file(test1.txt CMAKE_CURRENT_BINARY_DIR COPYONLY)
configure_file(test2.txt CMAKE_CURRENT_BINARY_DIR COPYONLY)
configure_file(input.txt CMAKE_CURRENT_BINARY_DIR COPYONLY)

add_executable(Day11 main.cpp)
