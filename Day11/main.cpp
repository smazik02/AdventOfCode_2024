#include <cctype>
#include <cmath>
#include <fstream>
#include <iostream>
#include <sstream>
#include <streambuf>
#include <string>
#include <vector>

std::vector<uint64_t> parseFile(const std::string& filename);

int number_of_digits(uint64_t num);

int main(int argc, char* argv[]) {
    auto fileContent = parseFile(argv[1]);

    // std::cout << "Initial arrangement:\n";
    // for (const auto num: fileContent) {
    //     std::cout << num << " ";
    // }
    // std::cout << '\n';

    for (auto iter = 0; iter < 75; iter++) {
        std::printf("%d\n", iter);
        int idx = 0;
        for (idx = 0; idx < fileContent.size(); ++idx) {
            auto stone = fileContent[idx];

            if (stone == 0)
                fileContent[idx] = 1;

            else if (const int digit_count = number_of_digits(stone); digit_count % 2 == 0) {
                uint64_t rhs = stone % static_cast<uint64_t>(std::pow(10, digit_count/2));
                uint64_t lhs = stone / static_cast<uint64_t>(std::pow(10, digit_count/2));
                fileContent[idx] = lhs;
                fileContent.insert(fileContent.begin() + idx + 1, rhs);
                ++idx;
            }

            else
                fileContent[idx] *= 2024;
        }
        // std::printf("After %d blink:\n", iter);
        // for (const auto num: fileContent) {
        //     std::cout << num << " ";
        // }
        // std::cout << '\n';
    }

    std::printf("%lu stones", fileContent.size());

    return 0;
}

std::vector<uint64_t> parseFile(const std::string& filename) {
    std::vector<uint64_t> numbers;
    std::ifstream file(filename);

    if (!file) {
        std::cerr << "Error: Could not open file " << filename << std::endl;
        return numbers; // Return an empty vector
    }

    std::string line;
    while (std::getline(file, line)) {
        std::istringstream iss(line);
        uint64_t number;

        while (iss >> number) {
            numbers.push_back(number);
        }
    }

    file.close();
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
