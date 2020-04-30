# Running Unit Test
Test are written with Ruby, rspec so you need to setup Ruby environment. 
Depending on your OS you can setup Ruby by [this link](https://www.ruby-lang.org/en/documentation/installation/)

## Install `bundler`
When Ruby setup is done, you can install bundler

```shell script
gem install bundler
```

## Install requirements with bundler
When bundler is installed, you can install requirements by bundler 

```shell script
# go to root folder where Gemfile is present
cd testsozluk/
bundle install
```

## Run a spec file
```shell script
# go to test folder
cd testsozluk/src/test
rspec specs/validate_json_files_spec.rb 
```

### Run whole tests
```shell script
~/P/p/t/t/s/test (develop ⚡) rspec specs/

Starting test: File Format Check terms.json should be a valid json file
.
Starting test: File Format Check terms.json each term should have `term` and `meaning`
.
Starting test: File Format Check terms.json should not have duplicated keys/terms
.

Finished in 0.0081 seconds (files took 0.69206 seconds to load)
3 examples, 0 failures

```