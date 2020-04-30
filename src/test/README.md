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

## Run test
```shell script
# go to test folder
cd testsozluk/src/test
rspec specs/validate_json_files_spec.rb 
```
