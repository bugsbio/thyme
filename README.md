# thyme
[![Build Status](https://travis-ci.org/bugsbio/thyme.svg?branch=master)](https://travis-ci.org/bugsbio/thyme)

Thyme is a simple scheduler that allows running of jobs to cron schedules. So far it supports two kinds of job: firing an Amazon SNS notification, and triggering a Travis build.

To run Thyme in a docker container:

```bash
docker run --name thyme -e TRAVIS_API_TOKEN=$TRAVIS_API_TOKEN -v /my-jobs-dir:/code/jobs bugsbio/thyme
```

Where `/my-jobs-dir` is a directory on the host machine that contains `.edn` manifest files for the jobs you'd like to schedule.

There are two valid forms of manifest file (one for SNS jobs, one for Travis builds).

### SNS Job

```clojure
;; /my-jobs/test-job.edn
{:name "Thyme Test Job"                                  ;; a descriptive name for the job
 :id "jobs.test"                                         ;; a unique id for the job, used by Quartz
 :type :sns                                              ;; indicates the type of the job - `:sns` for a job that fires an SNS notification
 :topic "test-thyme"                                     ;; the SNS topic to publish to
 :trigger {:id "triggers.test" :schedule "0 * * * * ?"}  ;; the job trigger. currently only cron triggers, in this format, are supported
 :payload {:foo "bar" :baz 123}}                         ;; the edn that will be sent as the body of the SNS notification
```

### Travis job

```clojure
{:name "Nightly integration test run"                                  ;; a descriptive name for the job
 :id "jobs.integration-tests"                                          ;; a unique id for the job, used by Quartz
 :type :travis                                                         ;; indicates the type of the job - `:travis` for a job that triggers a travis build
 :org "myorg"                                                          ;; github org of the project to be built
 :repo "integration-tests"                                             ;; repo to be built
 :pro? true                                                            ;; indicates if the repo is in public (open-source) travis, or the paid pro version
 :trigger {:id "triggers.integration-tests" :schedule "0 0 3 * * ?"}}  ;; the job trigger. currently only cron triggers, in this format, are supported
```
