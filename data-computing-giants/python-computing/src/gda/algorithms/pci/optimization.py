# -*- coding: utf-8 -*-

"""
优化.
@author: zhoujiagen
Created on 26/10/2018 1:04 PM
"""

import csv
import pprint
import time
import random
import math

from gda.algorithms.pci import WORKING_DIR

DEBUG = True

people = [
    ('Seymour', 'BOS'),  # name, origin
    ('Franny', 'DAL'),
    ('Zooey', 'CAK'),
    ('Walt', 'MIA'),
    ('Buddy', 'ORD'),
    ('Les', 'OMA')]

destination = 'LGA'


# schedule.txt
# origin, destination, departure time, arrival time, price
# LGA,OMA,6:19,8:13,239
def parse_schedule():
    with open(WORKING_DIR + "schedule.txt", "rt") as f:
        reader = csv.reader(f)
        rows = []
        for row in reader:
            rows.append(row)
    return [
        {
            "origin": row[0],
            "destination": row[1],
            "departure_time": row[2],
            "arrival_time": row[3],
            "price": int(row[4]),
        }
        for row in rows[1:]]


def _get_minutes_in_day(date_str):
    ts = time.strptime(date_str, "%H:%M")
    return ts.tm_hour * 60 + ts.tm_min


def show_schedule():
    schedule = parse_schedule()
    pprint.pprint(schedule)
    import matplotlib.pyplot as plt

    for s in schedule:
        departure_time = s["departure_time"]
        arrival_time = s["arrival_time"]
        middle_time = float(departure_time + arrival_time) / 2
        price = s["price"]
        plt.plot([departure_time, arrival_time], [price, price])
        plt.plot([middle_time, middle_time], [0, price])
    plt.show()
    plt.close()


def get_flights():
    schedule = parse_schedule()
    flights = {}
    for s in schedule:
        flights.setdefault((s["origin"], s["destination"]), [])
        flights[(s["origin"], s["destination"])].append(
            {"departure_time": s["departure_time"],
             "arrival_time": s["arrival_time"],
             "price": s["price"]})
    for flight in flights.keys():
        flights[flight] = sorted(flights[flight], key=lambda item: item["departure_time"])
    return flights


def show_solution(solution):
    from prettytable import PrettyTable
    """
    展示结果.
    :param solution: [(person1-departure-flight-index, person1-return-departure-flight-index), ...]
    :return:
    """
    flights = get_flights()
    table = PrettyTable(["person", "origin", "out", "return"])
    table.align = "l"
    for i in range(len(solution)):
        name = people[i][0]
        origin = people[i][1]
        out_flight = flights[(origin, destination)][solution[i][0]]
        return_flight = flights[(destination, origin)][solution[i][1]]
        table.add_row([name, origin,
                       "{}-{}(${})".format(out_flight["departure_time"], out_flight["arrival_time"],
                                           out_flight["price"]),
                       "{}-{}(${})".format(return_flight["departure_time"], return_flight["arrival_time"],
                                           return_flight["price"])])
    print(table)


def cost_of_solution(solution):
    """
    计算结果的成本.
    :param solution: [(person1-departure-flight-index, person1-return-departure-flight-index), ...]
    :return:
    """
    flights = get_flights()
    # 1 price and time of flight
    total_price_of_flight = 0
    total_time_of_flight = 0
    out_latest_arrival_time = 0
    return_earliest_departure_time = 24 * 60
    for i in range(len(solution)):
        origin = people[i][1]
        out_flight = flights[(origin, destination)][solution[i][0]]
        return_flight = flights[(destination, origin)][solution[i][1]]
        total_price_of_flight += out_flight["price"] + return_flight["price"]
        total_time_of_flight += \
            _get_minutes_in_day(out_flight["arrival_time"]) - _get_minutes_in_day(
                out_flight["departure_time"]) + _get_minutes_in_day(
                return_flight["arrival_time"]) - _get_minutes_in_day(
                return_flight["departure_time"])
        if _get_minutes_in_day(out_flight["arrival_time"]) > out_latest_arrival_time:
            out_latest_arrival_time = _get_minutes_in_day(out_flight["arrival_time"])
        if _get_minutes_in_day(return_flight["departure_time"]) < return_earliest_departure_time:
            return_earliest_departure_time = _get_minutes_in_day(return_flight["departure_time"])

    # 2 waiting time
    total_wait_time = 0
    for i in range(len(solution)):
        origin = people[i][1]
        out_flight = flights[(origin, destination)][solution[i][0]]
        return_flight = flights[(destination, origin)][solution[i][1]]
        total_wait_time += out_latest_arrival_time - _get_minutes_in_day(out_flight["arrival_time"])
        total_wait_time += _get_minutes_in_day(return_flight["departure_time"]) - return_earliest_departure_time

    return total_price_of_flight + total_time_of_flight * 0.1 + total_wait_time * 0.5


def random_searching(iterations=100, cost_function=cost_of_solution):
    """随机搜索."""
    best_cost = 999999
    best_solution = None

    flights = get_flights()

    for i in range(iterations):
        _solution = [(random.randint(0, len(flights[(origion, destination)]) - 1),
                      random.randint(0, len(flights[(destination, origion)]) - 1)) for _, origion in people]
        cost = cost_function(_solution)
        if cost < best_cost:
            best_cost = cost
            best_solution = _solution
            if DEBUG:
                print("DEBUG>>> Found more better solution: {}".format(cost))
                show_solution(_solution)

    return best_solution, best_cost


def hill_climbing(cost_function=cost_of_solution):
    """爬坡法."""

    def _delta_variable(low_bound, high_bound, value, delta=1):
        """计算相邻值."""
        result = []
        if not (value - delta < low_bound or value + delta > high_bound):
            result.append(value - delta)
            result.append(value + delta)
        return result

    flights = get_flights()
    _solution = [(random.randint(0, len(flights[(origion, destination)]) - 1),
                  random.randint(0, len(flights[(destination, origion)]) - 1)) for _, origion in people]

    while True:
        # 1 prepare neighbours
        neighbours = []
        for i in range(len(_solution)):
            origin = people[i][1]
            out_index = _solution[i][0]
            return_index = _solution[i][1]
            max_out_index = len(flights[(origin, destination)]) - 1
            max_return_index = len(flights[(destination, origin)]) - 1
            # how to handle this when facing more variables???
            # delta: 1
            out_deltas = _delta_variable(0, max_out_index, out_index, 1)
            return_deltas = _delta_variable(0, max_return_index, return_index, 1)
            for j in range(len(out_deltas)):
                for k in range(len(return_deltas)):
                    neighbours.append(_solution[0:i] + [(out_deltas[j], return_deltas[k])] + _solution[i + 1:])

        # 2 find best in neighbours
        current_cost = cost_function(_solution)
        best_cost = current_cost
        for sol in neighbours:
            cost = cost_function(sol)
            if cost < best_cost:
                if DEBUG:
                    print("DEBUG>>> Found more better solution: {}".format(cost))
                    show_solution(_solution)
                best_cost = cost
                _solution = sol

        if best_cost == current_cost:
            break

    return _solution, cost_function(_solution)


def simulated_annealing(cost_function=cost_of_solution, temperature=1000.0, cool_factor=0.95, step=1):
    """模拟退火方法."""

    flights = get_flights()
    _solution = [(random.randint(0, len(flights[(origion, destination)]) - 1),
                  random.randint(0, len(flights[(destination, origion)]) - 1)) for _, origion in people]

    while temperature > 0.1:
        i = random.randint(0, len(people) - 1)
        direction = random.randint(-step, step)
        origin = people[i][1]
        out_index = _solution[i][0]
        return_index = _solution[i][1]
        max_out_index = len(flights[(origin, destination)]) - 1
        max_return_index = len(flights[(destination, origin)]) - 1

        __solution = _solution[:]
        if _in(0, max_out_index, out_index + direction) \
                and _in(0, max_return_index, return_index + direction):
            __solution[i] = (out_index + direction, return_index + direction)
        _solution_cost = cost_function(_solution)
        __solution_cost = cost_function(__solution)
        p = pow(math.e, (-__solution_cost - _solution_cost) / temperature)

        if __solution_cost < _solution_cost or random.random() < p:
            if DEBUG:
                print("DEBUG>>> Use more better solution: {}".format(__solution_cost))
                show_solution(_solution)
            _solution = __solution

        temperature = temperature * cool_factor

    return _solution, cost_function(_solution)


def _in(low_bound, high_bound, value):
    if low_bound <= value <= high_bound:
        return True
    else:
        return False


def genetic_algorithm(population_size=50, step=1, p_mutate=0.2, p_keep=0.2, iterations=100,
                      cost_function=cost_of_solution):
    """遗传算法."""

    def mutate(solution):
        index = random.randint(0, len(people) - 1)
        (out_index, return_index) = solution[index]
        origin = people[index][1]
        max_out_index = len(flights[(origin, destination)]) - 1
        max_return_index = len(flights[(destination, origin)]) - 1
        if _in(0, max_out_index, out_index + step) \
                and _in(0, max_return_index, return_index + step):
            return solution[0:index] + [(out_index + step, return_index + step)] + solution[index + 1:]
        elif _in(0, max_out_index, out_index - step) \
                and _in(0, max_return_index, return_index - step):
            return solution[0:index] + [(out_index - step, return_index - step)] + solution[index + 1:]
        else:
            return solution[:]

    def cross_over(solution1, solution2):
        index = random.randint(0, len(people) - 1)
        return solution1[0:index] + solution2[index:]

    def simulate_solution():
        return [(random.randint(0, len(flights[(origion, destination)]) - 1),
                 random.randint(0, len(flights[(destination, origion)]) - 1))
                for _, origion in people]

    flights = get_flights()
    populations = [simulate_solution() for _ in range(population_size)]
    keep_count = int(p_keep * population_size)

    # do we need a stop condition here, since may stop evolve at an early stage
    for i in range(iterations):
        scores = [(cost_function(sol), sol) for sol in populations]
        scores.sort()
        ranked_solutions = [sol for _, sol in scores]
        if DEBUG:
            print(scores)

        populations = ranked_solutions[0:keep_count]
        while len(populations) < population_size:
            if random.random() < p_mutate:
                _index = random.randint(0, keep_count - 1)
                populations.append(mutate(ranked_solutions[_index]))
            else:
                _index1 = random.randint(0, keep_count - 1)
                _index2 = random.randint(0, keep_count - 1)
                populations.append(cross_over(ranked_solutions[_index1], ranked_solutions[_index2]))

        if DEBUG:
            print("DEBUG>>> [{}] current best solution: {}".format(i, cost_function(populations[0])))
            show_solution(populations[0])

    return populations[0], cost_function(populations[0])


if __name__ == '__main__':
    # out the first day, return the second day
    # pprint.pprint(get_flights())
    # solution = [(0, 0) for _, _ in people]
    # show_solution(solution)

    # 随机搜索
    # pprint.pprint(random_searching())
    # 爬坡法
    # pprint.pprint(hill_climbing())
    # 模拟退火
    # pprint.pprint(simulated_annealing())
    # 遗传算法
    pprint.pprint(genetic_algorithm())
