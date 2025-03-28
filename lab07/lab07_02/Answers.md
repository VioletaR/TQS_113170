# a)
- How long did the API calls take on average, minimum and maximum?

http_req_duration..............: avg=147.98ms min=4.76ms  med=123.44ms max=744.81ms

- How many requests were made?

http_reqs......................: 2052    102.566153/s

- How many requests failed? (i.e., whose HTTP status code was not 200)

http_req_failed................: 0.00%   0 out of 2052

# e)
> k6 run -e K6_WEB_DASHBOARD=true test_stages2.js
> [K6 WebDashBoard](http://127.0.0.1:5665/) // transient

# f)
Update the previous configuration to use 120+ VUs instead of 20 VUs and re-run the load test
scenario. Check the results. What happened?

- With the increased load the number of requests failed and the amount of time spent have increased, failing in both tests.
  - ✗ http_req_duration..............: avg=1.01s
  - ✗ http_req_failed................: 49.57% 3535 out of 7131

# g)
> k6 run --out experimental-prometheus-rw test_stages2.js
> [Prometheus](http://127.0.0.1:3000/) // not transient

What check failed on the last run you made and up to what value?
- 
