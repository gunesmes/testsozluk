# frozen_string_literal: true

require 'pathname'

RSpec.context 'Image existancy check' do
	describe 'image of' do
		before(:context) do
			@dictionary_img = Pathname.new("#{File.dirname(__FILE__ )}/../../img/dictionary.png")
			@license_img = Pathname.new("#{File.dirname(__FILE__ )}/../../img/license.png")
		end

		it "dictionary should be present" do
			expect(@dictionary_img).to exist
			expect(@dictionary_img).to be_file
		end

		it "license should be present" do
			expect(@license_img).to exist
			expect(@license_img).to be_file
		end
	end
end
