1
2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
31
32
33
34
35
36
37
38
39
40
41
42
43
44
45
46
47
48
49
50
51
# Project Estimation  
Authors: Elia Fontana, Andrea Palomba, Leonardo Perugini, Francesco Sattolo
Date: 27/04/2021
Version: 1.0
# Contents
- [Estimate by product decomposition]
- [Estimate by activity decomposition ]
# Estimation approach
# Estimate by product decomposition
### 
|                                                                                                         | Estimate         |
| ------------------------------------------------------------------------------------------------------- | ---------------- |
| NC =  Estimated number of classes to be developed                                                       | 23 classes       |
| A = Estimated average size per class, in LOC                                                            | 160 LOC          |
| S = Estimated size of project, in LOC (= NC * A)                                                        | 3680 LOC         |
| E = Estimated effort, in person hours (here use productivity 10 LOC per person hour)                    | 368 person hours |
| C = Estimated cost, in euro (here use 1 person hour cost = 30 euro)                                     | 11040 €          |
| Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ) | 2.3 weeks        |
# Estimate by activity decomposition
### 
| Activity name           | Estimated effort (person hours) |
| ----------------------- | ------------------------------- |
| Requirements            | 70                              |
| GUI Design              | 15                              |
| Class Design            | 30                              |
| Coding                  | 100                             |
| Testing                 | 50                              |
| Documentation           | 20                              |
| Database Setup          | 25                              |
| Training                | 20                              |
| Configuration procedure | 5                               |
| Installation            | 10                              |
| Total                   | 345                             |

Estimated calendar time, in calendar weeks (Assume team of 4 people, 8 hours per day, 5 days per week ): 2 weeks, 1 day

### Gantt chart
Insert here Gantt chart with above activities

![](Images/Gantt_chart.png)