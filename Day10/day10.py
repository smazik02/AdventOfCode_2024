import pprint


def traverse_graph_unique_ends(trailhead, adj_list, reached_ends):
    if file_content[trailhead[0]][trailhead[1]] == 9:
        reached_ends.add((trailhead[0], trailhead[1]))
        return

    if trailhead not in adj_list:
        return

    for neighbor in adj_list[trailhead]:
        traverse_graph_unique_ends(neighbor, adj_list, reached_ends)


def traverse_graph_start_unique_ends(trailhead, adj_list):
    reached_ends = set()
    traverse_graph_unique_ends(trailhead, adj_list, reached_ends)
    return len(reached_ends)


def count_unique_ends(trailheads, adj_list):
    unique_total = 0
    for trailhead in trailheads:
        unique = traverse_graph_start_unique_ends(trailhead, adj_list)
        unique_total += unique
    return unique_total


def traverse_graph_unique_trails(trailhead, adj_list):
    if file_content[trailhead[0]][trailhead[1]] == 9:
        return 1

    if trailhead not in adj_list:
        return 0

    return sum([traverse_graph_unique_trails(neighbor, adj_list) for neighbor in adj_list[trailhead]])


def traverse_graph_start_unique_trails(trailheads, adj_list):
    return sum([traverse_graph_unique_trails(trailhead, adj_list) for trailhead in trailheads])


def main():
    global file_content
    with open("input.txt") as file:
        file_content = [[int(height, 10) for height in line.strip()]
                        for line in file.readlines()]

    adj_list = dict()
    trailheads = []

    for row_idx, row in enumerate(file_content):
        for col_idx, height in enumerate(row):
            if height == 9:
                continue

            if height == 0:
                trailheads.append((row_idx, col_idx))

            allowed_coords = []
            if row_idx > 0: allowed_coords.append((row_idx - 1, col_idx))
            if row_idx < len(row) - 1: allowed_coords.append((row_idx + 1, col_idx))
            if col_idx > 0: allowed_coords.append((row_idx, col_idx - 1))
            if col_idx < len(file_content) - 1: allowed_coords.append((row_idx, col_idx + 1))

            for coord_x, coord_y in allowed_coords:
                if file_content[coord_x][coord_y] - height == 1:
                    adj_list.setdefault((row_idx, col_idx), []).append((coord_x, coord_y))

    # PART 1
    unique_paths = count_unique_ends(trailheads, adj_list)
    print(unique_paths)

    # PART 2
    unique_trails = traverse_graph_start_unique_trails(trailheads, adj_list)
    print(unique_trails)


if __name__ == "__main__":
    main()
