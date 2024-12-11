#include <array>
#include <cmath>
#include <cstdint>
#include <fstream>
#include <iostream>
#include <map>
#include <ranges>
#include <sstream>
#include <string>

std::map<uint64_t, uint64_t> parseFile(const std::string& filename);

int number_of_digits(uint64_t num);

void iterate_new_map(const std::map<uint64_t, uint64_t> &old_map, std::map<uint64_t, uint64_t> &new_map);

int main(int argc, char* argv[]) {
    int oldMap = 0;
    int newMap = 1;
    auto numberMaps = std::array<std::map<uint64_t, uint64_t>, 2>();
    numberMaps[0] = parseFile(argv[1]);

    for (auto iter = 0; iter < 75; iter++) {
        numberMaps[newMap].clear();

        iterate_new_map(numberMaps[oldMap], numberMaps[newMap]);

        oldMap = oldMap == 0 ? 1 : 0;
        newMap = newMap == 0 ? 1 : 0;
    }

    uint64_t stone_count = 0;
    for (const auto amount : std::views::values(numberMaps[oldMap])) {
        stone_count += amount;
    }

    std::printf("%lu stones\n", stone_count);

    exit(0);
}

void iterate_new_map(const std::map<uint64_t, uint64_t> &old_map, std::map<uint64_t, uint64_t> &new_map) {
    for (const auto [stone, amount]: old_map) {
        if (stone == 0)
            new_map[1] += amount;

        else if (const int digit_count = number_of_digits(stone); digit_count % 2 == 0) {
            uint64_t rhs = stone % static_cast<uint64_t>(std::pow(10, digit_count/2));
            uint64_t lhs = stone / static_cast<uint64_t>(std::pow(10, digit_count/2));
            new_map[rhs] += amount;
            new_map[lhs] += amount;
        }

        else
            new_map[stone*2024] += amount;
    }
}

std::map<uint64_t, uint64_t> parseFile(const std::string& filename) {
    std::map<uint64_t, uint64_t> numbers;
    std::ifstream file(filename);

    if (!file) {
        std::cerr << "Error: Could not open file " << filename << std::endl;
        exit(1);
    }

    std::string line;
    while (std::getline(file, line)) {
        std::istringstream iss(line);
        uint64_t number;

        while (iss >> number) {
            numbers[number]++;
        }
    }

    return numbers;
}

int number_of_digits(uint64_t num) {
    int count = 0;
    while (num > 0) {
        num /= 10;
        ++count;
    }

    return count;
}
