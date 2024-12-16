import os
import re
import sys
from copy import deepcopy

ROBOT_REGEX = r"p=(-?\d+,-?\d+) v=(-?\d+,-?\d+)"


def pretty_print(arr):
    for row in arr:
        for x in row:
            print(x, end=' ')
        print()


def main():
    with open("inputs/input.txt") as file:
        file_content = file.read().strip()

    # Part 1
    robots = []
    robot_matches = re.finditer(ROBOT_REGEX, file_content)
    for match in robot_matches:
        pos_start_x, pos_start_y = map(int, match.group(1).split(','))
        vel_x, vel_y = map(int, match.group(2).split(','))
        robots.append({
            "start": (pos_start_x, pos_start_y),
            "vel": (vel_x, vel_y)
        })

    x_size = 101
    y_size = 103
    seconds = 100
    coord_count = {}
    coord_set = set()
    for robot in robots:
        end_x = (robot["start"][0] + (seconds * robot["vel"][0])) % x_size
        end_y = (robot["start"][1] + (seconds * robot["vel"][1])) % y_size

        if (end_x, end_y) not in coord_count:
            coord_count[(end_x, end_y)] = 0
        coord_count[(end_x, end_y)] += 1
        coord_set.add((end_x, end_y))

    # print(coord_count)
    # pprint.pp(coord_count)

    upper_left = {"xrange": range(0, x_size // 2), "yrange": range(0, y_size // 2)}
    upper_right = {"xrange": range(x_size // 2 + 1, x_size), "yrange": range(0, y_size // 2)}
    lower_left = {"xrange": range(0, x_size // 2), "yrange": range(y_size // 2 + 1, y_size)}
    lower_right = {"xrange": range(x_size // 2 + 1, x_size), "yrange": range(y_size // 2 + 1, y_size)}

    upper_left_count = 0
    upper_right_count = 0
    lower_left_count = 0
    lower_right_count = 0
    for coord in coord_set:
        if coord[0] in upper_left["xrange"] and coord[1] in upper_left["yrange"]:
            upper_left_count += coord_count[coord]
        if coord[0] in upper_right["xrange"] and coord[1] in upper_right["yrange"]:
            upper_right_count += coord_count[coord]
        if coord[0] in lower_left["xrange"] and coord[1] in lower_left["yrange"]:
            lower_left_count += coord_count[coord]
        if coord[0] in lower_right["xrange"] and coord[1] in lower_right["yrange"]:
            lower_right_count += coord_count[coord]

    result = upper_left_count * upper_right_count * lower_left_count * lower_right_count
    print(result)

    init_arr = [[' ' for _ in range(y_size)] for _ in range(x_size)]
    # pretty_print(init_arr)

    sec = 1
    while (True):
        for i in range(80):
            print(sec + i)
            cur_arr = deepcopy(init_arr)
            for robot in robots:
                cur_x = (robot["start"][0] + (sec + i * robot["vel"][0])) % x_size
                cur_y = (robot["start"][1] + (sec + i * robot["vel"][1])) % y_size
                cur_arr[cur_x][cur_y] = 'X'
            pretty_print(cur_arr)
            sys.stdout.flush()
        input()
        sec += 100


if __name__ == '__main__':
    main()
