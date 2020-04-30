# frozen_string_literal: true

require 'json'

RSpec.context 'File Format Check' do
	describe 'terms.json' do
    before(:context) do
			file = File.open("#{File.dirname(__FILE__ )}/../../../terms.json")
			@file_content = file.read
    end

    it "should be a valid json file" do
			expect(valid_json?(@file_content)).to be true
		end

		it "each term should have `term` and `meaning`" do
			expect(has_key_pairs?(@file_content)).to be true
		end

    it "should not have duplicated keys/terms" do
			expect(has_duplicated_terms?(@file_content)).to be true
    end
	end
end
