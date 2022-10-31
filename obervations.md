# Methodology
Time taken and Max memory obtained by averaging result of `Maximum resident set size` and `Elapsed (wall clock) time` obtained from the output of `/usr/bin/time -v` over 4 runs.

# Basic Benches

| Case        | Java Version | Time Taken(s) | Max Memory Used(kb) |
|-------------|--------------|---------------|---------------------|
| Oracle Java | 19           | 41.435        | 298,408             |
| OpenJDK     | 19           | 40.405        | 296,341             |
| GraalVM CE  | 19           | 48.990        | 360,789             |
| Oracle Java | 17           | 56.852        | 694,314             |
| OpenJDK     | 17           | 54.850        | 767,616             |
| GraalVM CE  | 17           | 49.712        | 354,282             |
| GraalVM EE  | 17           | 33.853        | 343,274             |

# Native image

| Case            | Time Taken | Max Memory Used |
|-----------------|------------|-----------------|
| GraalVM CE      | 2:43.87    | 105920          |
| GraalVM EE      | 2:04.96    | 80832           |
| GraalVM EE(PGO) | TODO       | TODO            |