import re

with open("input.txt") as file:
    program = file.read().strip()

MUL_REGEX = r"mul\(([0-9]+,[0-9]+)\)"

sum = 0
instructions = re.finditer(MUL_REGEX, program)
for instruction in instructions:
    lhs, rhs = instruction.group(1).split(',')
    sum += int(lhs) * int(rhs)

print(f"The sum is {sum}")

# PART TWO
INS_REGEX = r"mul\(([0-9]+,[0-9]+)\)|do(n't)?\(\)"

instructions = re.finditer(INS_REGEX, program)
state = True
sum = 0
for instruction in instructions:
    if instruction.group(0) == "don't()":
        state = False
    elif instruction.group(0) == "do()":
        state = True

    elif state:
        lhs, rhs = instruction.group(1).split(',')
        sum += int(lhs) * int(rhs)

print(f"The enabled sum is {sum}")
